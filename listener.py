#/usr/local/env python3

from flask import Flask, jsonify, request, make_response

app = Flask(__name__)

@app.route('/', methods=['POST'])
def create_task():
    print(request.headers)
    print(request.json)
    return "", 200
