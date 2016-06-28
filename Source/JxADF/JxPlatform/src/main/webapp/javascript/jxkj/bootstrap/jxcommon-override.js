//com.jxtech.tag.fragment
//inherit [jxcommon.js]window.getTableData
//inherit [jxcommon.js]window.afterFragmentLoad

//com.jxtech.tag.table
// todo override [jxcommon.js]window.selectTableTr
// todo override [jxcommon.js]window.ckOneSelectHandler

function bindTableEvent(tableId){
    
}

function selectTableTr(tr,e){
    
}

function loadNavigation(homepage){
    if(top.location == self.location){ 
        var data='<nav class="navbar navbar-default navbar-fixed-bottom"><div align="center">'
                +'      <a id="nav_home" style="font-size: 25px" href="'+homepage+'"> '
                +'          <span class="glyphicon glyphicon-home"></span>'
                +'      </a>'
                +'</div></nav>';
        $('body').append(data);
    } 
}
