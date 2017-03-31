'use strict'

var Koa = require('koa')
var app = new Koa()
var fs = require('fs')
var ejs = require('ejs')

var site = "http://khrystal.tunnel.2bdata.com"

var createTimestamp = function() {
  return parseInt(new Date().getTime() / 1000, 10) + ''
}

app.use(function *(next) {
  var that = this
  if (this.url.indexOf('/index') > -1) {
    console.log('index request success')
    var source = fs.readFileSync('index.html', 'utf-8')
    this.body = source
    return next
  }
  else if (this.url.indexOf('titlebar') > -1) {
    console.log('titlebar request success')
    var source = fs.readFileSync('titlebar.html', 'utf-8')
    this.body = source
    return next
  }
  else if (this.url.indexOf('navigator') > -1) {
    console.log('navigator request success')
    var source = fs.readFileSync('navigator.html', 'utf-8')
    this.body = source
    return next
  }
  else if (this.url.indexOf('jssdkTest') > -1) {
    console.log('jssdkTest request success')
    var source = fs.readFileSync('jssdkTest.html', 'utf-8')
    this.body = source
    return next
  }
  else if (this.url.indexOf('closePage') > -1) {
    console.log('closePage request success')
    var source = fs.readFileSync('closePage.html', 'utf-8')
    this.body = source
    return next
  }
  else if (this.url.indexOf('closeGroup') > -1) {
    console.log('closeGroup request success')
    var source = fs.readFileSync('closeGroup.html', 'utf-8')
    this.body = source
    return next
  }
  else if (this.url.indexOf('zhsdk') > -1) {
    console.log('zhsdk request success')
    var source = fs.readFileSync('zhsdk-0.0.1.js', 'utf-8')
    this.body = source
    return next
  }
  else {
    console.log('index success')
    var source = fs.readFileSync('index.html', 'utf-8')
    this.body = source
    return next
  }
  yield next
})

app.listen(1234)
console.log('Listening :1234')
