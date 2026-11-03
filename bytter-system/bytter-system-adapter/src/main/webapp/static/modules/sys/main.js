'use strict';
if(!bt.widgetContainer){bt.widgetContainer=[];}
var vm = avalon.define({
    $id: "homeController",
    editMode: false,//是否处于编辑模式
    sortParam:{order:'sort', dir:1},
    container: [],
    refreshColumn:{},
    show: function(){
        this.config.isShow = true
    },
    toggleEditMode:function(){
        this.editMode = !this.editMode;
    },
    checkAttrId:function (el) {
        if(el.id){
            return {id:el.id};
        }
        var uuid = this.uuid(32,62);
        el.id = uuid;
        return {id:uuid};
    },
    uuid:function(len, radix) {
        var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        var uuid = [], i;
        radix = radix || chars.length;
        if (len) {
            for (var i = 0; i < len; i++){ uuid[i] = chars[0 | Math.random()*radix];}
        } else {
            var r;
            uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
            uuid[14] = '4';
            for (var i = 0; i < 36; i++) {
                if (!uuid[i]) {
                    r = 0 | Math.random()*16;
                    uuid[i] = chars[(i === 19) ? (r & 0x3) | 0x8 : r];
                }
            }
        }
        return uuid.join('');
    },
    save:function(){
        var allName = [];
        var data = vm.container.$model;
        for (var row in data) {
            for (var columns in data[row].columns) {
                allName.push(data[row].columns[columns].name);
            }
        }
        var hash = {};
        for(var i in allName) {
            if(hash[allName[i]]) {
                parent.layer.alert('不能同时有两个相同的组件',{icon:2});
                return;
            }
            hash[allName[i]] = true;
        }

        var that = this;
        layer.confirm('是否保存当前页面布局？', {icon: 3}, function(index){
            $.ajax({
                type:"post",
                url:basePath + "/sys/home/save",
                dataType: "json",
                contentType: "application/json",
                data:JSON.stringify(that.container.$model),
                success:function(result) {
                    parent.layer.alert(result.msg, {icon: result.success?1:2});
                }
            });
        });

    },
    removeRow:function(rows){
        var rowsObj = vm.container.$model;
        if(rowsObj.length <= 1){
            alert("首页至少保留一行容器！");
            return;
        }
        vm.container.remove(rows);
    },
    colunmEdit:function(column){
        vmModel.currentColumn = column;
        vmModel.modelConfig.isShow = true
    },
    refreshWidget:function(rowIndex,colIndex,col){
        if(bt.widgetContainer){
            if(col.refresh){
                return;
            }
            col.refresh = true;
            var widgetContainerKey = col.widgetName + col.id;
            var widget = bt.widgetContainer[widgetContainerKey];
            widget.refreshFunc(function(){
                col.refresh = false;
            });
        }
    },
    resetDefault:function(){
        var that = this;
        parent.layer.confirm("是否重置为默认布局？",{icon:3},function(){
            $.get(basePath + "/sys/home/resetDefault", function(result){
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if(result.success){
                    that.newest();
                }
            },"JSON");
        });
    },
    newest:function(){
        var that = this;
        $.get(basePath + "/sys/home/newest", function(result){
            if(result){
                result = JSON.parse(result);
                that.container = result;
            }else{
                that.container = that.emptyData();
            }
            $('.right-side .layui-row').sortable({
                opacity: .5,
                items: ".column:not(.drag-box)",
                handle: ".component-content-head-drag",
                update: function (event, ui) {
                    that.sortColumn($(this),event, ui);
                }
            }).disableSelection();
        },'JSON');
    },
    sortColumn : function(jqObj,event, ui){
        var that = this;
        var rowId = jqObj.attr('id');
        avalon.each(that.container, function(index, el){
            if(el.id === rowId){
                var rowIds = jqObj.sortable("toArray");
                avalon.each(el.columns, function(indexc, elc){
                    elc.sort = that.getIndex(rowIds,elc.id);
                });
            }
        });
    },
    getIndex : function(rowIds,key){
        for(var i=0;i<rowIds.length;i++){
            var k = rowIds[i];
            if(k===key){
                return i;
            }
        }
    },
    emptyData : function(){
        return [
            {
                "id":"",
                "sort":0,
                "columns":[
                    {"id":"","sort":0,"column":4,"name":"用户信息","showHead":true,"refresh":false,"widgetName":"userInfo","height":200,"content":"/sys/home/userInfo"},
                    {"id":"","sort":2,"column":4,"name":"待办信息","showHead":true,"refresh":false,"widgetName":"todoInfo","height":200,"content":"/bpm/my/todoInfo"},
                    {"id":"","sort":1,"column":4,"name":"常用菜单","showHead":true,"refresh":false,"widgetName":"usuallyMenu","height":200,"content":"/sys/menuCollect/widget"}
                ]
            },
            // {
            //     "id":"",
            //     "sort":1,
            //     "columns":[
            //         {"id":"","column":4,"showHead":true,"refresh":false,"sort":0,"name":"融资余额汇总","widgetName":"CmsBalanceGather","content":"/cms/report/cmsbalancegather/gatherViewPirPage?corpId=gatherCorpId&curId=naturalCurId","height":200},
            //         {"id":"","column":8,"showHead":true,"refresh":false,"sort":1,"name":"资金存量汇总","widgetName":"widgetCapitalStock","content":"/bi/capitalStock/widgetCapitalStock","height":200}
            //     ]
            // },
            // {
            //     "id":"",
            //     "sort":2,
            //     "columns":[
            //         {"id":"","column":6,"showHead":true,"refresh":false,"sort":0,"name":"测试图表A","widgetName":"ChartA","content":"/sys/home/chartA","height":400},
            //         {"id":"","column":6,"showHead":true,"refresh":false,"sort":0,"name":"测试图表B","widgetName":"ChartB","content":"/sys/home/chartB","height":400}
            //     ]
            // }
        ];
    }
});
function getAvalon(){
    return vm;
}

var vmModel = avalon.define({
    $id: "modalController",
    currentColumn:{},
    modelConfig: {
        isShow:false,
        success: function(e){
            e.close();
        },
        title:'首页组件选择'
    }
});


avalon.ready(function() {
    vm.newest();
    $('.right-side .layui-fluid').sortable({
        opacity: .5,
        handle: ".row-drag",
        update: function (event, ui) {
            var rowIds = $(this).sortable("toArray");
            //如果为新增的行
            if(!ui.item.attr('id')){
                var columnNumber = ui.item.find('input').val();
                var columns = columnNumber.split(' ');
                var newRow;
                for(var i = 0; i<rowIds.length; i++){
                    if(!rowIds[i]){
                        var uuid = vm.uuid(32,62);
                        rowIds[i] = uuid;
                        var columnBox = [];
                        for(var n=0;n<columns.length;n++){
                            columnBox.push({id:vm.uuid(32,62),column:parseInt(columns[n]),showHead:true,sort:0,name:"",widgetName:"",content:"",height:200});
                        }
                        newRow = {
                            id:uuid,
                            sort:i,
                            columns:columnBox
                        }
                    }
                }
                avalon.Array.ensure(vm.container,newRow);
                ui.item.detach();
            }
            //更新行顺序
            avalon.each(vm.container, function(index, el){
                el.sort = vm.getIndex(rowIds,el.id);
            });
        }
    }).disableSelection();

    $('.nav-list-item').draggable({
        connectToSortable: ".right-side .layui-fluid",
        helper: "clone",
        stop:function(){
            $('.right-side .layui-row').sortable({
                opacity: .5,
                handle: ".component-content-head-drag",
                update: function (event, ui) {
                    vm.sortColumn($(this),event, ui);
                }
            }).disableSelection();
        }
    }).disableSelection();
});
