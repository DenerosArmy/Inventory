var express = require('express'),
	connect = require('connect'),
	ejs = require('ejs'),
	engine = require('ejs-locals'),
	mongoose = require('mongoose'),
	socketIO = require('socket.io'),
    util = require('util'),
    couchdb = require('felix-couchdb');
    client = couchdb.createClient(5984, 'pythonscript.denerosarmy.com'), 
    db = client.db('inventory'); 
	//models = require('./models');
	
var app = express.createServer();
	
var errorCallback = function(err) {
	if (err) {
		console.log(err);
	}
};

app.configure(function() {
	app.engine('ejs', engine);
	app.set('view engine', 'ejs');
	app.set('views', __dirname + '/views');
	app.use(express.bodyParser());
	app.use(express.methodOverride());
	app.use(express.cookieParser());
	app.use(app.router);
	app.use('/', express.static(__dirname + '/public'));
});

app.get('/room/:title', function(req, res) {
    db.getDoc(req.params.title, function(er,doc) { 
        console.log("Error is" + JSON.stringify(er))
        console.log(doc);
	    res.render('room', {title: req.params.title, data:doc})

    });
});

/*
app.post('/join/:title', function(req, res) {
    db.saveDoc(req.params.title, {req.body.name:req.body.items});
});
*/
app.post('/join/:title', function(req, res){
    console.log(req.body);
});


var port = process.env.PORT || 8000;
app.listen(port);

var io = socketIO.listen(app);

io.sockets.on('connection', function(client) {
	console.log("connected");
	client.emit('hello', {yo: 'dawg'});
	client.on('search items', function(data) {
		// data = {origin: String, query: String}
		console.log(data);
	})
});

io.sockets.on('name', function(name, title) {
	console.log("name received", name);
	io.sockets.emit("new items", {items: db[title][name]});
});

io.sockets.on('disconnect', function(name, title) {
	
});
