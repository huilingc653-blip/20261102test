var datatable;
bt.use(['form','datatable','select','dicts','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        
        checkbox:true,
        order: [[1,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/allocation/bisgatherpolicy/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('netbank:allocation:bisGatherPolicy:apply'),
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.enable'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    enable(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.stop'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    stop(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"gatherPolicyCode",bSortable:true,className:'center'},
            {data:"gatherPolicyName",bSortable:true},
            {data:"gatherType",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("bisGatherPolicy.gatherType",full.gatherType);
                }
            },
            {data:"defDate",bSortable:true,className:'center'},
            {data:"frqMode",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("bisGatherPolicy.frqMode",full.frqMode);
                }
            },
            {data:"serialDay",bSortable:true,
            	render: function ( data, type, full, meta ) {
            		//调拨周期：4-按日，8-按周，16-按月，32-按季，64-按年
            		if(full.frqMode=="4"){
            			return bt.lang("table.bisGatherPolicy.daySplit")+full.serialDay+bt.lang("table.bisGatherPolicy.day");
            		}else if(full.frqMode=="8"){
            			return bt.lang("table.bisGatherPolicy.week")+bt.dict("bisGatherPolicy.week",full.serialDay);
            		}else if(full.frqMode=="16"){
            			return bt.lang("table.bisGatherPolicy.month")+bt.dict("bisGatherPolicy.month",full.serialDay);
            		}else{
            			return full.serialDay;
            		}
                }
            },
            {data:"failHandleMethod",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("bisGatherPolicy.failHandleMethod",full.failHandleMethod);
                }
            },
            {data:"validSign",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("sf_yn",full.validSign);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:allocation:bisGatherPolicy:apply')){
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                }
            	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
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
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/allocation/bisgatherpolicy/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/bisgatherpolicy/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/allocation/bisgatherpolicy/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/allocation/bisgatherpolicy/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/allocation/bisgatherpolicy/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/allocation/bisgatherpolicy/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function enable(data){
    var len = data.length;
    if (len < 1) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchEnable'),function(){
        $.post(basePath + "/allocation/bisgatherpolicy/batchEnable", {ids: ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
function stop(data){
    var len = data.length;
    if (len < 1) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchStop'),function(){
        $.post(basePath + "/allocation/bisgatherpolicy/batchStop", {ids: ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}