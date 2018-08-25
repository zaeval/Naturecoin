import hashlib
import json, ast
from time import time
from urlparse import urlparse
from uuid import uuid4
import requests
from flask import Flask, jsonify, request, render_template
import atexit
import datetime
import os
from apscheduler.schedulers.background import BackgroundScheduler



class Blockchain:
    def __init__(self):
        self.current_transactions = []
        self.chain = []
        self.nodes = set()
        # Logs for Visualization
        self.timestamps = ['x', time(),]
        self.num_users = ['users', 0,]
        self.num_transactions = ['transactions', 0,]
        self.num_coins = ['coins', 0,]


        # Create the genesis block
        self.new_block(previous_hash='1', proof=100)



    def register_node(self, address):
        """
        Add a new node to the list of nodes

        :param address: Address of node. Eg. 'http://192.168.0.5:5000'
        """

        parsed_url = urlparse(address)
        self.nodes.add(parsed_url.netloc)

    def valid_chain(self, chain):
        """
        Determine if a given blockchain is valid

        :param chain: A blockchain
        :return: True if valid, False if not
        """

        last_block = chain[0]
        current_index = 1

        while current_index < len(chain):
            block = chain[current_index]
            print('{}'.format(last_block))
            print('{}'.format(block))
            print("\n-----------\n")
            # Check that the hash of the block is correct
            if block['previous_hash'] != self.hash(last_block):
                return False

            # Check that the Proof of Work is correct
            if not self.valid_proof(last_block['proof'], block['proof'], block['previous_hash']):
                return False

            last_block = block
            current_index += 1

        return True

    def resolve_conflicts(self):
        """
        This is our consensus algorithm, it resolves conflicts
        by replacing our chain with the longest one in the network.

        :return: True if our chain was replaced, False if not
        """

        node_list = self.nodes
        new_chain = None

        # We're only looking for chains longer than ours
        max_length = len(self.chain)

        # Grab and verify the chains from all the nodes in our network
        for node in node_list:
            response = requests.get('http://{}/chain'.format(node))

            if response.status_code == 200:
                length = response.json()['length']
                chain = response.json()['chain']

                # Check if the length is longer and the chain is valid
                if length > max_length and self.valid_chain(chain):
                    max_length = length
                    new_chain = chain

        # Replace our chain if we discovered a new, valid chain longer than ours
        if new_chain:
            self.chain = new_chain
            return True

        return False


    def broadcast_block(self, block):
        node_list = self.nodes

        for node in node_list:
            try:
                print(block)
                r = requests.post('http://{}/blocks/add'.format(node), json=block)


                print(r.content.message + " in {}".format(node))
            except:
                print("Failed to broadcast. The block is already placed or manipulation is suspected.")
                return





    def new_block(self, proof, previous_hash=None):
        """
        Create a new Block in the Blockchain
        :param proof: <int> The proof given by the Proof of Work algorithm
        :param previous_hash: (Optional) <str> Hash of previous Block
        :return: <dict> New Block
        """

        block = {
            'index': len(self.chain) + 1,
            'timestamp': time(),
            'transactions': self.current_transactions,
            'proof': proof,
            'previous_hash': previous_hash or self.hash(self.chain[-1]),
        }

        # Forge block into blockchain
        self.chain.append(block)

        # Broadcast block to other nodes
        if len(self.nodes) != 0:
            self.broadcast_block(block)


        self.timestamps.append(block['timestamp'])
        self.num_transactions.append(len(block['transactions']))
        self.num_users.append(len(set([i['sender'] for i in block['transactions']] + [j['recipient'] for j in block['transactions']])))
        self.num_coins.append(sum([int(i['amount']) for i in block['transactions']]) + self.num_coins[-1])



        # Reset the current list of transactions
        self.current_transactions = []
        return block

    def new_transaction(self, sender, recipient, amount):
        """
        Creates a new transaction to go into the next mined Block

        :param sender: Address of the Sender
        :param recipient: Address of the Recipient
        :param amount: Amount
        :return: The index of the Block that will hold this transaction
        """
        self.current_transactions.append({
            'sender': sender,
            'recipient': recipient,
            'amount': amount,
        })

        return self.last_block['index'] + 1

    @property
    def last_block(self):
        return self.chain[-1]

    @staticmethod
    def hash(block):
        """
        Creates a SHA-256 hash of a Block

        :param block: Block
        """

        # We must make sure that the Dictionary is Ordered, or we'll have inconsistent hashes
        block_string = json.dumps(block, sort_keys=True).encode()
        return hashlib.sha256(block_string).hexdigest()

    def proof_of_work(self, last_block):
        """
        Simple Proof of Work Algorithm:
         - Find a number p' such that hash(pp') contains leading 4 zeroes, where p is the previous p'
         - p is the previous proof, and p' is the new proof
        """

        last_proof = last_block['proof']
        last_hash = self.hash(last_block)

        proof = 0
        while self.valid_proof(last_proof, proof, last_hash) is False:
            proof += 1

        return proof

    @staticmethod
    def valid_proof(last_proof, proof, last_hash):
        """
        Validates the Proof

        :param last_proof: Previous Proof
        :param proof: Current Proof
        :return: True if correct, False if not.
        """

        guess = '{0}{1}{2}'.format(last_proof, proof, last_hash).encode()
        guess_hash = hashlib.sha256(guess).hexdigest()

        return guess_hash[:4] == "0000"


# Instantiate the Node
app = Flask(__name__, static_url_path='')

# Generate a globally unique address for this node
node_identifier = str(uuid4()).replace('-', '')

# Instantiate the Blockchain
blockchain = Blockchain()

# Check if the chaindata exists
if os.path.isfile('chain.json'):
   with open('chain.json', 'r') as f:
        chaindata = json.loads(f.read())
        # Remove 'u' chars
        chaindata = ast.literal_eval(json.dumps(chaindata))
        blockchain.timestamps = ['x', chaindata[0]['timestamp'], chaindata[0]['timestamp']]
        # Analyze the data
        for block in chaindata:
            blockchain.timestamps.append(block['timestamp'])
            blockchain.num_transactions.append(len(block['transactions']))
            blockchain.num_users.append(len(set([i['sender'] for i in block['transactions']] + [j['recipient'] for j in block['transactions']])))
            blockchain.num_coins.append(sum([int(i['amount']) for i in block['transactions']]) + blockchain.num_coins[-1])
        # Change blockchain data
        blockchain.chain = chaindata

        print(blockchain.timestamps)
        print(blockchain.num_transactions)
@app.route('/mine', methods=['GET'])
def mine():
    # We run the proof of work algorithm to get the next proof...
    last_block = blockchain.last_block
    last_proof = last_block['proof']
    proof = blockchain.proof_of_work(last_block)

    # We must receive a reward for finding the proof.
    # The sender is "0" to signify that this node has mined a new coin.
    if len(blockchain.current_transactions) == 0:
        response = {'message': 'There is no current transaction to confirm a block'}
        return jsonify(response), 501

    mine_tx = {'sender': "0", 'recipient':node_identifier, 'amount': 25}
    if mine_tx not in blockchain.current_transactions:
        blockchain.new_transaction(
            sender=mine_tx['sender'],
            recipient= mine_tx['recipient'],
            amount = mine_tx['amount']
            )

    # Forge the new Block by adding it to the chain
    block = blockchain.new_block(proof)

    # Update the chaindata
    with open('chain.json', 'w') as outfile:
        json.dump(blockchain.chain, outfile)


    response = {
        'message': "New Block Forged",
        'index': block['index'],
        'transactions': block['transactions'],
        'proof': block['proof'],
        'previous_hash': block['previous_hash'],
    }
    return jsonify(response), 200

# Add broadcast block
@app.route('/blocks/add', methods=['POST'])
def add_block():
    block = request.get_json() or request.form

    # Check the required fields
    required = ['index', 'previous_hash', 'proof', 'timestamp', 'transactions' ]
    if not all(k in block for k in required):
        return 'Missing values', 400
    # Remove 'u' chars
    block = ast.literal_eval(json.dumps(block))
    # Validate the block
    last_block = blockchain.last_block
    last_proof = last_block['proof']
    if blockchain.valid_proof(last_proof, block.get('proof'), block.get('previous_hash')):
        blockchain.chain.append(block)
        response = {
        'message': 'Block is broadcasted',
        }

    # Update Chaindata
    with open('chain.json', 'w') as outfile:
        json.dump(blockchain.chain, outfile)

    return jsonify(response), 200




@app.route('/transactions/new', methods=['POST'])
def new_transaction():
    values = request.get_json() or request.form

    print(values)
    # Check that the required fields are in the POST'ed data
    required = ['sender', 'recipient', 'amount']
    if not all(k in values for k in required):
        return 'Missing values', 400
    # Remove 'u' chars
    values = ast.literal_eval(json.dumps(values))
    # Create a new Transaction
    index = blockchain.new_transaction(values["sender"], values["recipient"], float(values["amount"]))

    response = {'message': 'Transaction will be added to Block {}'.format(index)}
    return jsonify(response), 200



@app.route('/chain', methods=['GET'])
def full_chain():
    response = {
        'chain': blockchain.chain,
        'length': len(blockchain.chain),
    }
    return jsonify(response), 200


@app.route('/nodes/register', methods=['POST'])
def register_nodes():
    values = request.get_json()

    nodes = values.get('nodes')
    if nodes is None:
        return "Error: Please supply a valid list of nodes", 400

    for node in nodes:
        blockchain.register_node(node)

    response = {
        'message': 'New nodes have been added',
        'total_nodes': list(blockchain.nodes),
    }
    return jsonify(response), 201


@app.route('/nodes/resolve', methods=['GET'])
def consensus():
    replaced = blockchain.resolve_conflicts()

    if replaced:
        response = {
            'message': 'Our chain was replaced',
            'new_chain': blockchain.chain
        }
    else:
        response = {
            'message': 'Our chain is authoritative',
            'chain': blockchain.chain
        }

    return jsonify(response), 200


@app.route('/', methods=['GET'])
def visualize():



    response = {
        'timestamps': blockchain.timestamps,
        'num_users': blockchain.num_users,
        'num_transactions': blockchain.num_transactions,
        'num_coins': blockchain.num_coins,
        'chain': blockchain.chain
    }

    print(response)
    return render_template("index.html", title = 'Status', response=response)



# Mine(Confirm a block) from server
def mine_server():
    # Throw if there is no current transaction
    if len(blockchain.current_transactions) == 0:
        response = {'message': 'There is no current transaction to confirm a block'}
        print(response)
        return 501


    # We run the proof of work algorithm to get the next proof...
    last_block = blockchain.last_block
    last_proof = last_block['proof']
    proof = blockchain.proof_of_work(last_block)

    # We must receive a reward for finding the proof.
    # The sender is "0" to signify that this node has mined a new coin.
    mine_tx = {'sender': "0", 'recipient':node_identifier, 'amount': 25}
    if mine_tx not in blockchain.current_transactions:
        blockchain.new_transaction(
            sender=mine_tx['sender'],
            recipient= mine_tx['recipient'],
            amount = mine_tx['amount']
            )

    # Forge the new Block by adding it to the chain
    block = blockchain.new_block(proof)

    # Update Chaindata
    with open('chain.json', 'w') as outfile:
        json.dump(blockchain.chain, outfile)


    response = {
        'message': "New Block Forged",
        'index': block['index'],
        'transactions': block['transactions'],
        'proof': block['proof'],
        'previous_hash': block['previous_hash'],
    }
    print(response)
    return 200



# define the job which has to be monitored
def monitor():
    mine_server()



if __name__ == '__main__':
    from argparse import ArgumentParser

    parser = ArgumentParser()
    parser.add_argument('-p', '--port', default=80, type=int, help='port to listen on')
    args = parser.parse_args()
    port = args.port
    # init BackgroundScheduler job
    scheduler = BackgroundScheduler()
    # in your case you could change seconds to hours
    scheduler.add_job(monitor, trigger='interval', seconds=3)
    scheduler.start()
    app.run(host='0.0.0.0', port=port)
