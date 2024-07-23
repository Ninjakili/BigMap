#! /bin/bash
cd /Users/i589221/Documents/BigMap/maegges/frontend
npm i
cd ../backend
python3  -m venv ./.venv
source ./.venv/bin/activate
pip install -r requirements.txt
python main.py