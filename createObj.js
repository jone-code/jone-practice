var obj = new Object();
obj.name = "zs"; 
obj.age = 21;
obj.showName = function(){
	alert(this.name);
}
obj.showAge = function(){
	alert(this.age);
}


function createObj(){ 
	var obj = new Object(); //创建对象 

	obj.name = "Koji"; 
	obj.age = 21; 
	obj.showName = function(){ 
	alert(this.name); 
	} 
	obj.showAge = function(){ 
		alert(this.age); 
	} 

	return obj; //返回对象 
} 












function createObj(name, age){ 
	var obj = new Object(); //创建对象 

	obj.name = name; 
	obj.age = age; 
	obj.showName = showName; 
	obj.showAge = showAge; 

	return obj; //返回对象 
} 

function showName(){ //函数也是一个对象 
alert(this.name); 
} 

function showAge(){ 
alert(this.age); 
} 

