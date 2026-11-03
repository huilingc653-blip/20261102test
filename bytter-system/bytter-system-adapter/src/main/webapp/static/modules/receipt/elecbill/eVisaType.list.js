bt.use(['form','datatable','selectTree','select','corp','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.mulselect.init();
    bt.corp.init();

    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/evisatype/evisatype/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                // text: '<i class="layui-icon">&#xe642;</i> '+bt.lang('common.button.edit'),
                // action: function ( e, dt, node, config ) {
                //     var data = dt.rows('.selected').data();
                //     var len = data.length;
                //     var rowData = data.splice(0,len);
                //     layer.msg(JSON.stringify(rowData));
                // }
            // },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"netName",bSortable:true},
            {data:"billType",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("eVisaType.billType",full.billType);
                }
            },
            {data:"billName",bSortable:true},
            {data:"picWidth",bSortable:true,className:'textRight'},
            {data:"picHeight",bSortable:true,className:'textRight'},
            {data:"validSign",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("eVisaType.validSign",full.validSign);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                if(data.validSign == 1){
                	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode">|</span>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="toInvalid(this)">'+bt.lang("common.button.tovoid")+'</a>';
                }else{
                	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode">|</span>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="toRecover(this)">'+bt.lang("common.button.recover")+'</a>';
                }
                return html;
            }}
        ]
    });
    //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
    //下拉树
    $.each($("div[type='selecttree']"),function(i,item){
        var filter = {enable:true};
        if($(item).attr("dictField")){
            filter.idKey =  $(item).attr("dictField");
        }
        if($(item).attr("dictText")){
            filter.pIdKey =  $(item).attr("dictText");
        }
        bt.selectTree.init({
            url: $(item).attr("fieldHref"),
            elem:"[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            selected: $(item).attr("defaultVal"),
            setting:{
                data:{
                    simpleData: filter
                }
            }
        });
    });
});
function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/evisatype/evisatype/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/evisatype/evisatype/add",
        end:function(){
        	table && table.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/evisatype/evisatype/view/" + data.id
    });
}
function update(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/evisatype/evisatype/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/evisatype/evisatype/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
//作废签章类型
function toInvalid(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	parent.layer.confirm(bt.lang('common.message.confirm.tovoid'),function(){
    	 $.post(basePath + "/evisatype/evisatype/operate?id="+ data.id + "&opType=0", function(result){
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    	});
}
//恢复签章类型
function toRecover(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.recover'),function(){
        $.post(basePath + "/evisatype/evisatype/operate?id="+ data.id + "&opType=1" , function(result){
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/evisatype/evisatype/delete/" + data.id, function(result){
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}