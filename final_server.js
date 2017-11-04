
var express = require('express');
var url = require('url');
var app = express();
var http = require('http');
var bodyParser = require('body-parser');

app.use(bodyParser.urlencoded({ extended: false }));

var automatic = 1;
var manual = 0;
var isOccupied=true;
var light = 0;
var temperature = 0;
var detectMotion = 0;
var ldrday = 0;
var query;
var query1;
var query2;

function updatedata(){

    var pathname = "/query";
    var options = {
        host: "10.42.0.240",
        port: 80,
        path: pathname
    };

    var body = '';
    http.get(options, function(responseFromRemoteApi) {
        responseFromRemoteApi.setEncoding('utf8');
        responseFromRemoteApi.on('data', function(chunk) {
            // When this event fires we append chunks of 
            // response to a variable
            //console.log(chunk);
            body += chunk;
        });
        responseFromRemoteApi.on('end', function(){
            // We have the complete response from Server B (stackoverflow.com)
            // Send that as response to client
            //console.log("ended");
            query1 = JSON.parse(body);
            console.log(query1);
            isOccupied = query1.isOccupied;

            if(automatic == 1 && isOccupied && !query1.lights)
                forwardhttp("/light/",0,null);

            if(automatic == 1 && !isOccupied && query1.lights)
                forwardhttp("/light/",1,null);

        });

    }).on('error', function(e) {
        console.log('Error when calling remote API: ' + e.message);
    });

}

function forwardhttp(device,operation,res){
    var pathname = device  + operation;
    console.log(pathname);
    var options = {
        host: "10.42.0.240",
        port: 80,
        path: pathname
    };

    var body = '';
    http.get(options, function(responseFromRemoteApi) {
        responseFromRemoteApi.setEncoding('utf8');
        responseFromRemoteApi.on('data', function(chunk) {
            // When this event fires we append chunks of 
            // response to a variable
            console.log(chunk);
            body += chunk;
        });
        responseFromRemoteApi.on('end', function(){
            // We have the complete response from Server B (stackoverflow.com)
            // Send that as response to client
            console.log("ended");
            if(res != null)
            res.send(body);
        });
    }).on('error', function(e) {
        console.log('Error when calling remote API: ' + e.message);
        if(res != null)
        res.send("{'status':'error'}");
    });
}

function validatemanual(device, operation,res){
    if(manual == 1){
        forwardhttp(device,operation,res);
    }
    else
        res.send("{'status':'error'}");
}

app.get('/manual',function(req,res){
    manual = 1;
    automatic = 0;
    res.send("{'status':'done'}");
});

app.get('/automatic',function(req,res){
    automatic = 1; manual = 0;
    res.send("{'status':'done'}");
});

app.get('/light/0', function(req,res){
    validatemanual("/light/",0,res);
});

app.get('/light/1', function(req,res){
    validatemanual("/light/",1,res);
});

app.get('/query', function(req,res){
    var s1 = JSON.stringify(query1);
    var s2 = JSON.stringify(query2);
    s1 = s1.slice(0,s1.length-1);
    s2 = s2.slice(1,s2.length);
    s1 = "["+s1 +',' + s2+"]";
    res.send(s1);
});

app.get('/fan/0', function(req,res){
    validatemanual1("/fan/",0,res);
});

app.get('/fan/1', function(req,res){
    validatemanual1("/fan/",1,res);
});

/*function updatedata(){
    console.log(".");
}*/

var server = app.listen(3000,function(err){
	if(err){
		throw err;
	}
	else{
		console.log('server started');
	}
    setInterval(updatedata,2000);
    setInterval(updatedata1,2000);
});

function updatedata1(){

    var pathname = "/query";
    var options = {
        host: "10.42.0.196",
        port: 80,
        path: pathname
    };

    var body = '';
    http.get(options, function(responseFromRemoteApi) {
        responseFromRemoteApi.setEncoding('utf8');
        responseFromRemoteApi.on('data', function(chunk) {
            // When this event fires we append chunks of 
            // response to a variable
            //console.log(chunk);
            body += chunk;
        });
        responseFromRemoteApi.on('end', function(){
            // We have the complete response from Server B (stackoverflow.com)
            // Send that as response to client
            //console.log("ended");
            query2 = JSON.parse(body);
            console.log(query2);
            temperature = query2.temperature;

            if(automatic == 1 && isOccupied)
                if(temperature > 25 && !(query2.fans))
                    forwardhttp1("/fan/",0,null);
                else if(temperature <= 25 && query2.fans)
                    forwardhttp1("/fan/",1,null);

            if(automatic == 1 && !isOccupied && query2.fans)
                forwardhttp1("/fan/",1,null);

        });

    }).on('error', function(e) {
        console.log('Error when calling remote API: ' + e.message);
    });

}

function forwardhttp1(device,operation,res){
    var pathname = device  + operation;
    console.log(pathname);
    var options = {
        host: "10.42.0.196",
        port: 80,
        path: pathname
    };

    var body = '';
    http.get(options, function(responseFromRemoteApi) {
        responseFromRemoteApi.setEncoding('utf8');
        responseFromRemoteApi.on('data', function(chunk) {
            // When this event fires we append chunks of 
            // response to a variable
            console.log(chunk);
            body += chunk;
        });
        responseFromRemoteApi.on('end', function(){
            // We have the complete response from Server B (stackoverflow.com)
            // Send that as response to client
            console.log("ended");
            if(res != null)
            res.send(body);
        });
    }).on('error', function(e) {
        console.log('Error when calling remote API: ' + e.message);
        if(res != null)
        res.send("{'status':'error'}");
    });
}

function validatemanual1(device, operation,res){
    if(manual == 1){
        forwardhttp1(device,operation,res);
    }
    else
        res.send("{'status':'error'}");
}
