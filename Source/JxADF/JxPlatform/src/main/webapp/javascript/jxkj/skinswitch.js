/**
 * 弹出切换皮肤的页面。
 * @param me
 * @param e
 */
function skinsSwitch(me,e){
	if($("#skins").html()==""){
		return;
	}
	$("#header").css("z-index","99");
	$("#skins").toggle();
}


function skinTo(url){
	if (url==null || ""==url){
		return;
	}
	WebClientBean.saveUserMetadata('HOMEPAGE',url,{
	        callback: function (data) {
                $("#skins").hide();
	            window.location.href=contextPath+url;
	        }
	});
}

function setCurrentSkin() {
    var skinName = (location.pathname.match(/skin[^/]+/) || ["skinClassics"])[0];
    skinName = skinName.replace(/[A-Z]/,function(match){return "-"+match.toLowerCase()});
    $("."+skinName).addClass("active");
}


$(document).ready(function() {
    $('.skin-header ul li').click(function () {
        $(this).addClass('on').siblings().removeClass('on');
        var styleName = this.getAttribute("skin");
        switchStylestyle(window.top, styleName);
        createCookie('style', styleName, 365);
    });
	var c = readCookie('style');
	if (c) switchStylestyle(window.top, c);

	if(!$("#skinswitch").is(":hidden")){
		$("#skinAction").on("mouseenter",function(){
			skinsSwitch();
		}).on("mouseleave",function(){
			$("#skins").hide();
		})
	}
    setCurrentSkin();
});

function switchStylestyle(winObj, styleName)
{
    console.info(winObj);
    $('link[title]', winObj.document).each(function(i)
    {
        var str = $(this).attr("href");
        var reg = /skin\/.+?\//;
        var skin_url = str.match(reg);
        console.info(skin_url);
        $(this).attr("href", str.replace(skin_url, "skin/" + styleName + "/"));
    });
    var frames = winObj.frames;
    for (var i = 0; i < frames.length; i++) {
        switchStylestyle(frames[i], styleName);//递归换肤

    };
}

function createCookie(name,value,days)
{
	if (days)
	{
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}
function readCookie(name)
{
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++)
	{
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1,c.length);
		if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
}
function eraseCookie(name)
{
	createCookie(name,"",-1);
}
