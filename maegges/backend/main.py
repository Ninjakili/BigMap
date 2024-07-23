from typing import Optional
from fastapi import FastAPI, Response
from fastapi.responses import JSONResponse, FileResponse
from starlette.responses import RedirectResponse
from contextlib import asynccontextmanager
import uvicorn
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import sqlite3
import pandas

class ID(BaseModel):
    id: str
    uID: str

class User(BaseModel):
    id: str

class Credentials(BaseModel):
    name: str
    password: str

class Review(BaseModel):
    nodeID: str
    userID: str
    rating: int
    review: Optional[str] = None



@asynccontextmanager
async def lifespan(app: FastAPI):
    yield
    conn.commit()
    cur.close()
    conn.close()

app = FastAPI(lifespan=lifespan)

conn = sqlite3.connect("./data/database.db")
cur = conn.cursor()
app.lastLogin = ""


origins = [
    "http://localhost:8000",
    "http://3.75.158.163",
    "http://3.125.183.140",
    "http://35.157.117.28",
    "http://127.0.0.1",
    "http://0.0.0.0" 
]


app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
async def root():
    return FileResponse("../frontend/index.html", media_type="text/html")
@app.get("/map.html")
async def map_html():
    return FileResponse("../frontend/map.html", media_type="text/html")
@app.get("/login.css")
async def root_css():
    return FileResponse("../frontend/login.css", media_type="text/css")
@app.get("/map.css")
async def map_css():
    return FileResponse("../frontend/map.css", media_type="text/css")
@app.get("/transformpolys.xsl")
async def xsl():
    return FileResponse("../frontend/transformpolys.xsl", media_type="text/xsl")
@app.get("/transformnodes.xsl")
async def xsl():
    return FileResponse("../frontend/transformnodes.xsl", media_type="text/xsl")
@app.get("/node_modules/ol/ol.css")
async def ol_css():
    return FileResponse("../frontend/node_modules/ol/ol.css", media_type="text/css")

@app.get("/code/map.js")
async def map_js():
    return FileResponse("../frontend/code/map.js", media_type="text/javascript")

@app.get("/images/logo.png")
async def logo_png():
    return FileResponse("../frontend/images/logo.png", media_type="image/png")
@app.get("/images/search.png")
async def search_png():
    return FileResponse("../frontend/images/search.png", media_type="image/png")


@app.get("/assign")
async def assign():
    print(app.lastLogin)
    if(app.lastLogin == ""):
        return JSONResponse(content={"url": "http://localhost:8000/"})
    tmp = app.lastLogin
    app.lastLogin = ""
    return JSONResponse(content={"name": tmp})

@app.post("/login")
async def login(cred: Credentials):
    cur.execute('SELECT name from users where name="' + cred.name + '" and password="' + cred.password + '"');
    if(cur.fetchall() == []):
        return JSONResponse(content={'message': 'Invalid credentials, please try again'})
    else:
        app.lastLogin=cred.name
        return JSONResponse(content={'message': 'http://localhost:8000/map.html'})

@app.post("/createAccount")
async def register(cred: Credentials):
    if(cred.name == "" or cred.password == ""):
        return JSONResponse(content={'message': 'Empty names or passwords are not allowed!', 'status': 'error'})
    try:
        cur.execute('INSERT INTO users VALUES ("'+cred.name+'", "'+cred.password+'")');
        cur.execute('SELECT * from users where name="'+cred.name+'"')  
        print(cur.fetchall())
        return JSONResponse(content={'message': 'Successfully created account!', 'status': 'success'})
    except:
        return JSONResponse(content={'message': 'Account with that name already exists', 'status': 'error'})
    
@app.post("/getNodes")
async def getNodes(user: User):
    cur.execute('SELECT xml, rating, review, nodes.nodeID as nodeID from nodes left join (SELECT userID, nodeID, rating, review from reviews where userID = "' + user.id + '") as reviews on nodes.nodeID=reviews.nodeID order by nodes.nodeID asc')
    nodes = cur.fetchall();
    cur.execute('SELECT nodeID, avg(rating) from reviews group by nodeID order by nodeID asc')
    avgs = cur.fetchall();
    cur.execute('SELECT nodeID from reviews where userID="' + user.id + '"')
    reviews = cur.fetchall();
    fullxml = '''<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<catalog>\n'''
    for node in nodes:
        fullxml += node[0].replace("\\n", "\n");
        hasAvg = False
        hasRating = False
        iterator = 0
        iterations = 0
        for review in reviews:
            if(review[0] == str(node[3])):
                hasRating = True
        for avg in avgs:
            if(avg[0] == str(node[3])):
                hasAvg = True
                iterations = iterator
            iterator += 1
        if(hasRating):
            fullxml += '''\t<tag k=\"rating\" v=\"''' + str(node[1]) + '''\"/>\n\t<tag k=\"review\" v=\"''' + str(node[2]) + '''\"/>\n\t<tag k=\"avg\" v=\"''' + str(avgs[iterations][1]) + '''\"/>\n  </node>\n'''
        elif(hasAvg):
            fullxml += '''\t<tag k=\"avg\" v=\"''' + str(avgs[iterations][1]) + '''\"/>\n  </node>\n'''
        else:
            fullxml += '''  </node>\n'''
    fullxml += "</catalog>\n"

    return Response(content=fullxml, media_type="application/xml")

@app.post("/giveReview")
async def giveReview(rev: Review):
    if(rev.review):
        cur.execute('INSERT INTO reviews VALUES ("' + rev.userID + '", "' + rev.nodeID + '", ' + str(rev.rating) + ', "' + rev.review + '") on conflict do UPDATE SET rating=' + str(rev.rating) + ', review="' + rev.review + '" WHERE userID="' + rev.userID + '" AND nodeID="' + rev.nodeID + '"');
    else:
         cur.execute('INSERT INTO reviews VALUES ("' + rev.userID + '", "' + rev.nodeID + '", ' + str(rev.rating) + ', "' + rev.review + '") on conflict do UPDATE SET rating=' + str(rev.rating) + ' WHERE userID="' + rev.userID + '" AND nodeID="' + rev.nodeID + '"');
    return JSONResponse(content={"message": "updated review of " + rev.userID + " on " + rev.nodeID + " to " + str(rev.rating)});

@app.post("/getPolys")
async def getPolys(user: User):
    cur.execute('SELECT first, second from polys join visits on polys.nodeID=visits.nodeID where visits.userID="' + user.id + '"')
    visited = cur.fetchall()

    fullxml = '''<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<catalog>\n'''
    
    for poly in visited:
        fullxml += (poly[0].replace("\\t", "\t").replace("\\n", "\n").replace("\\\"", "\"") + "true" + poly[1].replace("\\t", "\t").replace("\\n", "\n").replace("\\\"", "\"")) + "\n"

    fullxml += "</catalog>\n"
    return Response(content=fullxml, media_type="application/xml")

@app.post("/getCount")
async def getCount(user: User):
    cur.execute('SELECT count(first) from polys join visits on polys.nodeID=visits.nodeID where visits.userID="' + user.id + '"');
    count = cur.fetchall()[0][0];
    cur.execute('SELECT count(first) from polys');
    total = cur.fetchall()[0][0];
    return JSONResponse(content={'count': count, 'total':total});

@app.post("/clearVisits")
async def clear(user: User):
    cur.execute('DELETE from visits WHERE userID="' + user.id + '"')


@app.post("/visitAll")
async def visitAll(user: User):
    cur.execute('DELETE from visits WHERE userID="' + user.id + '"')
    cur.execute('INSERT INTO visits SELECT "' + user.id + '", nodeID from polys')
    
@app.post("/toggleVisited")
async def toggleVisited(id: ID):
    cur.execute('SELECT nodeID from visits where userID="' + id.uID + '" and nodeID=' + id.id + '')
    if(cur.fetchall() == []):
        cur.execute('INSERT INTO visits VALUES ("' + id.uID + '", '+ id.id + ');')
    else:
        cur.execute('DELETE from visits where nodeID=' + id.id + ' and userID="' + id.uID +'"')
    return {'id': id.id, 'uID': id.uID}

@app.get("/resetUsers")
async def reset():
    resetUsers()
    
def resetUsers():
    cur.execute('''DROP TABLE IF EXISTS users''')

def initDatabase():
    cur.execute('''CREATE TABLE IF NOT EXISTS nodes (
                nodeID INTEGER PRIMARY KEY,
                xml TEXT NOT NULL
    )''')

    cur.execute('''CREATE TABLE IF NOT EXISTS polys (
                nodeID INTEGER PRIMARY KEY, 
                first text NOT NULL, 
                second text NOT NULL
        );''')
    cur.execute('''CREATE TABLE IF NOT EXISTS visits (
                userID TEXT NOT NULL, 
                nodeID INTEGER NOT NULL, 
                PRIMARY KEY (userID, nodeID)
        );''')
    cur.execute('''CREATE TABLE IF NOT EXISTS users (
                name TEXT UNIQUE, 
                password TEXT NOT NULL,
                PRIMARY KEY (name) 
        );''')
    
    cur.execute('''CREATE TABLE IF NOT EXISTS reviews (
                userID TEXT NOT NULL,
                nodeID TEXT NOT NULL,
                rating INTEGER NOT NULL,
                review TEXT,
                PRIMARY KEY (userID, nodeID)
        );''')
    
    polys = pandas.read_csv("./data/polys.csv", delimiter=';', engine="python", on_bad_lines="warn")
    polys.to_sql('polys', conn, if_exists="replace", index = False)
    nodes = pandas.read_csv("./data/nodes.csv", delimiter=';;;', engine="python", on_bad_lines="warn")
    nodes.to_sql('nodes', conn, if_exists="replace", index = False)
    users = pandas.read_csv("./data/users.csv", delimiter=';', engine="python", on_bad_lines="warn")
    users.to_sql('users', conn, if_exists="replace", index = False)


if __name__ == "__main__":
    config = uvicorn.Config("main:app", host="localhost", port=8000)
    initDatabase()
    server = uvicorn.Server(config)
    server.run()