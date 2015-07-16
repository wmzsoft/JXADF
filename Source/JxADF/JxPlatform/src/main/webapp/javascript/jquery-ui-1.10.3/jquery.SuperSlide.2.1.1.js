// JavaScript Document
function getByClass(oParent,name)
{
	var aClass = oParent.getElementsByTagName('*');
	var arlt = [];
	for(var i=0; i<aClass.length; i++)
	{
		if(aClass[i].className==name)
		{
			arlt.push(aClass[i]);
		}
	}
	return arlt;
}

function getStyle(obj,name)
{
	if(obj.currentStyle)
	{
		return obj.currentStyle[name]
	}
	else
	{
		return getComputedStyle(obj,false)[name]
	}
}

function startMove(obj,styleName,iTarget)
{
	clearInterval(obj.timer)
	obj.timer = setInterval(function(){
          var now = 0;
		  if(styleName=='opacity')
		  {
			  now = Math.round((parseFloat(getStyle(obj,styleName))*100));
		  }
		  else
		  {
			  now = parseInt(getStyle(obj,styleName));
		  }
		  var speed = (iTarget-now)/6;
		  speed = speed>0?Math.ceil(speed):Math.floor(speed);
		  if(now == iTarget)
		  {
			  clearInterval(obj.timer)
		  }
		  else
		  {
			  if(styleName=='opacity')
			  {
				  obj.style.opacity = (now+speed)/100;
				  obj.style.filter = 'alpha(opacity='+(now+speed)+')'
			  }
			  else
			  {
				  obj.style[styleName] = now+speed+'px';
			  }
		  }
	},30)
}