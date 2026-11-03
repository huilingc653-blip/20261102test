$.ajax({
    type:"get",
    url:basePath + "/erp/swap/template/dynaDataSource",
    async:false,
    success : function (json) {
        if (!json||json.length===0) {
            return;
        }
        json = eval("(" + json + ")");
        var dynaDataSource = {};
        for(var i=0;i<json.length;i++){
            var temp = json[i];
            var tempGroupCode = temp.groupCode;
            if(!dynaDataSource[tempGroupCode]){
                dynaDataSource[tempGroupCode] = {};
            }
            if(!dynaDataSource[tempGroupCode][temp.code]){
                dynaDataSource[tempGroupCode][temp.code]={name:temp.name,code:temp.code};
            }
        }
        store.remove('dyna_data_source');
        store.set('dyna_data_source', dynaDataSource);
    }
});

var type = $("input[name='dealType']").val();
$.ajax({
    type:"get",
    url:basePath + "/erp/swap/template/dynaSource",
    data:"type="+type,
    async:false,
    success : function (json) {
        if (!json||json.length===0) {
            return;
        }
        json = eval("(" + json + ")");
        var dynaSource = {};
        for(var i=0;i<json.length;i++){
            var temp = json[i];
            var tempGroupCode = temp.groupCode;
            if(!dynaSource[tempGroupCode]){
                dynaSource[tempGroupCode] = {};
            }
            if(!dynaSource[tempGroupCode][temp.code]){
                dynaSource[tempGroupCode][temp.code]={name:temp.name,code:temp.code};
            }
        }
        store.remove('dyna_source');
        store.set('dyna_source', dynaSource);
    }
});

bt.dynaDataSourceContext = store.get('dyna_data_source');
bt.dynaDataSource = function (groupCode, code) {
    return bt.dynaDataSourceContext[groupCode] ? bt.dynaDataSourceContext[groupCode][code] ? bt.dynaDataSourceContext[groupCode][code].name : "" : "";
};
bt.dynaDataSourceGroup = function (groupCode) {
    return bt.dynaDataSourceContext[groupCode] ? bt.dynaDataSourceContext[groupCode] : [];
};


bt.dynaSourceContext = store.get('dyna_source');
bt.dynaSource = function (groupCode, code) {
    return bt.dynaSourceContext[groupCode] ? bt.dynaSourceContext[groupCode][code] ? bt.dynaSourceContext[groupCode][code].name : "" : "";
};
bt.dynaSourceGroup = function (groupCode) {
    return bt.dynaSourceContext[groupCode] ? bt.dynaSourceContext[groupCode] : [];
};

