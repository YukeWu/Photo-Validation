from flask import Flask
from flask import request
import os
import requests

BACKEND_HOST = os.getenv('BACKEND_HOST', 'http://localhost:8080')
# BACKEND_HOST = os.getenv('BACKEND_HOST', 'https://facial-recognition-backend.azurewebsites.net')
SERVER_PORT = os.getenv('SERVER_PORT', 8000)
DB_HOST = os.getenv('DB_HOST', 'http://localhost:8080')
NOSQL_HOST = os.getenv('NOSQL_HOST', 'http://localhost:8080')

# Create Flask app
app = Flask(__name__, static_url_path='/', static_folder='static')

@app.route('/')
def index():
    return app.send_static_file("index.html")

# Test endpoint, ignore
@app.route('/greeting', methods=['GET'])
def greeting():
    response = requests.get(BACKEND_HOST + '/greeting')
    return response.content, response.status_code

@app.route('/images', methods=['POST'])
def image():
    response = requests.post(BACKEND_HOST + '/images', data=request.data)
    return response.content, response.status_code

@app.route('/validate', methods=['POST'])
def validate():
    response = requests.post(BACKEND_HOST + '/validate', data=request.data)
    return response.content, response.status_code
    
@app.route('/form', methods=['POST'])
def form():
    response = requests.post(BACKEND_HOST + '/form', data=request.data)
    return response.content, response.status_code

@app.route('/users', methods=['POST'])
def users():
    # print(request.data)
    response = requests.post(DB_HOST + '/users', data=request.data)
    return response.content, response.status_code

if __name__ == '__main__': 
    app.run(host="0.0.0.0", port=SERVER_PORT, debug=True)