var organtypeDataTable,selectTree;
var dicts={};
var corp={};
var orgtIdCheck;
bt.use(['form','datatable','corp','dicts','element','dateType','btImport', 'treeGrid','selectTree','treeTable'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dateType.init();
    corp=bt.corp.init();
    dicts=bt.dicts.init();
    //组织架构表格
    organtypeDataTable = bt.datatable.init({
        elem:"#organtypeTb",
        ajax: {
            'url': basePath+"/sys/organtype/organtype/datatable"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        }, 
        colReorder: true,
        selectCss:true,
        checkbox:true,
        clickRowSelected:true,
        mulselect:false,
        btDrawCallback:function(setting){ //初始完成的回调方法
            bt.datatable.onCheckRow('#organtypeTb',{
                key:'id',
                value:orgtIdCheck,
                checked:true
            });
            orgtIdCheck="";
        },
        buttons:[
        	//新增
			{
				text: bt.lang('common.button.add'),
				hide:bt.isnotResource('sys:organtype:edit'),
				action: function ( e, dt, node, config ) {
					add();
				}
			},{
				extend: "colvis",
				text: bt.lang('common.button.theadHide'),
				columns: ':not(:first):not(:last)'
        	}
        ],
        columns:[
            {data:"orgtName",className:'center'},
            {data:"orgtVersion",className:'center'},
            {data:"releaseDate",className:'center',
                render: function (data, type, full, meta) {
            		if(full.releaseDate!=null){
                        return bt.date.format(full.releaseDate, "yyyy-MM-dd hh:mm:ss");
					}else{
            			return null;
					}
                }
			},
            {data:"effectiveDate",className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                //不存在网点单位且可编辑为禁用状态，也可删除组织架构。
                if((data.orgtVersion=='' || data.orgtVersion == null) && (data.isAddCorp == 0 || data.isAddCorp == null)){
                    html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteData(this)">' + bt.lang("common.button.delete") + '</a>';
                }
                if (data.validSign == 1) {//启用状态不可修改，只能初始化,初始化过的不能再次初始化,只能发布
					if((data.isInit == 0 || data.isInit == null) && (data.isAddCorp == 0 || data.isAddCorp == null)){
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="init(this)">' + bt.lang("sys.organtype.init") + '</a>';
					}else if((data.isAddCorp ==1 && data.isChangCorp == 1) || (data.orgtVersion==null || (data.isAddCorp ==1 && data.isChangCorp == ''))){
                        html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="release(this)">' + bt.lang("sys.organtype.release") + '</a>';
                    }
                }
                return html;
            }}
        ],
        btClickRowCallback:function(data){
            var orgData=bt.datatable.getSelectData('#organtypeTb');
            if (orgData.length == 1 ) {
                loadOrgtCorpTree(orgData[0].id,'');
            }else{
                resetCorpTable();
            }
        },
        exportFile: { //输出文档设置
			outputTitle: bt.lang('sys.organtype.organizational'),		//设置导出标题　默认为父级选项卡内容
			outputFilename: bt.lang('sys.organtype.organizational')		//设置导出文件名称　默认为父级选项卡内容或标题名称
		}
    });

    $('#corpContainer').hover(
        function () {
            $(this).css({'overflow-y': 'auto'});
        },
        function () {
            $(this).css({'overflow-y': 'hidden'});
        }
    );
});

//查询
function searchData(){
    var orgtName = $("#orgtName").val();
    var corpName = $("#corpName").val();
    if(corpName=="" ){
        organtypeDataTable.draw();
        loadOrgtCorpTree('null','');
    }
    var data=bt.datatable.getSelectData('#organtypeTb');
    if (data.length == 1 && corpName!="") {
        loadOrgtCorpTree(data[0].id,corpName);
    }else if(corpName!=""){
        organtypeDataTable.draw();
    }
}

//新增
function add(){
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/add",
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}
//修改
function update(el) {
    var data = organtypeDataTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    orgtIdCheck = id;
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/edit/" + id,
        end: function () {
            bt.datatable.refreshCurrentPage();
            loadOrgtCorpTree(orgtIdCheck,'');
        }
    });
}
//删除
function deleteData(el){
    var data = organtypeDataTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    var msg = bt.lang('common.message.confirm.delete');
    parent.layer.confirm(msg, function () {
        bt.loading.show();
        $.post(basePath + "/sys/organtype/organtype/delete", {"id": id}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
            bt.loading.hide();
        }, 'JSON');
    });
}
//查看
function view(el) {
    var data = organtypeDataTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    orgtIdCheck = id;
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/view/" + id,
        end: function () {
            bt.datatable.refreshCurrentPage();
            loadOrgtCorpTree(orgtIdCheck,'');
        }
    });
}
//初始化
function init(el){
    var data = organtypeDataTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    orgtIdCheck = id;
    var msg = bt.lang('sys.organtype.confirm.init');
    parent.layer.confirm(msg, function () {
        bt.loading.show();
        $.post(basePath + "/sys/organtype/organtype/init", {"id": id}, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }
            bt.loading.hide();
        }, 'JSON');
    });
}
//发布
function release(el) {
    var data = organtypeDataTable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    orgtIdCheck = id;
    parent.layer.open({
        type: 2,
        area: ['850px', '450px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/release/" + id,
        end: function () {
            bt.datatable.refreshCurrentPage();
            loadOrgtCorpTree(orgtIdCheck,'');
        }
    });
}

//初始化单位树形列表数据
function loadOrgtCorpTree(orgtId,corpName) {
    bt.loading.show();
    $("#corpContainer").html("");
    //通过Ajax获取数据
    var url = basePath+"/sys/organtype/organtype/getOrgCorpList";
    var param={
        orgtId:orgtId,
        corpName:corpName
    };
    $.ajax({
        type:"post",
        url:url,
        async:true,
        data:param,
        dataType:"json",
        success:function(res){
            setOrgtCorpTable(res);
            bt.loading.hide();
        },
        error:function(jqXHQ){
            setOrgtCorpTable('');
            bt.loading.hide();
            //alert("发生错误:"+jqXHQ.status);
        }
    });

}


//设置列表数据
function setOrgtCorpTable(data) {
    var tbodyHtmle ='<table id="corpTable" class="layui-table" >';
    //标题头
    tbodyHtmle +='<thead><tr >';
    //单位
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.corp")+'</th>';
    //组织类型
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.orgType")+'</th>';
    //单位属性
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.corpProperty")+'</th>';
    //管理单元
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.cellManage")+'</th>';
    //状态
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.status")+'</th>';
    //操作
    tbodyHtmle +='<th>'+bt.lang("common.table.thead.operation")+'</th>';
    tbodyHtmle +='</tr></thead>';
    //列表数据
    tbodyHtmle +='<tbody>';
    if(data!=''){
        for(var i=0;i<data.length;i++){
            tbodyHtmle += '<tr data-tt-id="'+data[i].corpId+'" ';
            if(data[i].parentId != null){
                tbodyHtmle += ' data-tt-parent-id="'+data[i].parentId+'" >';
            }else{
                tbodyHtmle += ' data-tt-parent-id="" >';
            }
            //单位
            tbodyHtmle += '<td>('+ data[i].corpCode + ')' + data[i].corpName+' </td>';
            //组织类型
            tbodyHtmle += '<td align="center">';
            if(data[i].type == 0){
                tbodyHtmle += '<span style="color:red;">' + data[i].typeName+'</span>';
            }else{
                tbodyHtmle += data[i].typeName;
            }
            tbodyHtmle += '</td>';
            //单位属性
            tbodyHtmle += '<td align="center">';
            if(data[i].unitAttributeName != null){
                tbodyHtmle +=data[i].unitAttributeName;
            }
            tbodyHtmle +='</td>';
            //管理单元
            tbodyHtmle += '<td align="center">';
            if(data[i].unitCmName != null){
                tbodyHtmle +=data[i].unitCmName;
            }
            tbodyHtmle +='</td>';
            //状态
            tbodyHtmle += '<td align="center">';
            if(data[i].status == 1){
                tbodyHtmle += data[i].statusName;
            }else{
                tbodyHtmle += '<span style="color:#cacaca;">' + data[i].statusName + '</span>';
            }
            tbodyHtmle += '</td>';
            //操作
            tbodyHtmle += '<td align="center">';
            //新增子单位
            if(data[i].status == 1){
                tbodyHtmle += '<a class="" href="#" onclick="addChildCorp(\''+data[i].orgtId+'\',\''+data[i].corpId+'\')">' + bt.lang("sys.organtype.addSubunits") + '</a>';
                tbodyHtmle += '<span class="text-explode">|</span>';
            }
            //查看
            tbodyHtmle += '<a class="" href="#" onclick="viewCorp(\''+data[i].id+'\',\''+data[i].corpId+'\',\''+data[i].parentId+'\')">' + bt.lang("common.button.view") + '</a>';
            tbodyHtmle += '<span class="text-explode">|</span>'
            if(data[i].status == 1){
                //编辑上下级
                tbodyHtmle += '<a class="" href="#" onclick="updateOrgtSupCorp(\''+data[i].orgtId+'\',\''+data[i].corpId+'\')">' + bt.lang("sys.organtype.editLevels") + '</a>';
                tbodyHtmle += '<span class="text-explode">|</span>'
            }
            //删除
            tbodyHtmle += '<a class="" href="#" onclick="deleteCorp(\''+data[i].id+'\',\''+data[i].orgtId+'\')">' + bt.lang("common.button.delete") + '</a>';
            tbodyHtmle += '</td>';
            tbodyHtmle += ' </tr>';
        }
    }
    tbodyHtmle +='</tbody></table>';
    $("#corpContainer").html(tbodyHtmle);
    /*if ($('#corpTable').data('treetable')){
        $('#corpTable').removeData("treetable").removeClass("treetable")
    }*/
    bt.treeTable.init({
        elem: '#corpTable',
        column: '0'
    });
    tableHeader();
}
//重置单位表格为无数据
function resetCorpTable(){
    $("#corpContainer").html("");
    var tbodyHtmle ='<table id="corpTable" class="layui-table" >';
    //标题头
    tbodyHtmle +='<thead><tr >';
    //单位
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.corp")+'</th>';
    //组织类型
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.orgType")+'</th>';
    //单位属性
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.corpProperty")+'</th>';
    //管理单元
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.cellManage")+'</th>';
    //状态
    tbodyHtmle +='<th>'+bt.lang("sys.organtype.status")+'</th>';
    //操作
    tbodyHtmle +='<th>'+bt.lang("common.table.thead.operation")+'</th>';
    tbodyHtmle +='</tr></thead>';
    //列表数据
    tbodyHtmle +='<tbody></tbody></table>';
    $("#corpContainer").html(tbodyHtmle);
    bt.treeTable.init({
        elem: '#corpTable',
        column: '0'
    });
}

//新增一级单位
function addOrgtCorp(){
    var data=bt.datatable.getSelectData('#organtypeTb');
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var orgtId = data[0].id;
    orgtIdCheck =orgtId;
    if(data[0].validSign != 1){
        layer.msg(bt.lang('sys.organtype.message.isValidSign'));
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '630px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/addOrgtFirstCorp/" + orgtId,
        end: function () {
           // bt.datatable.refreshCurrentPage();
            loadOrgtCorpTree(orgtId,'');
           // organtypeDataTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}
//新增子单位
function addChildCorp(orgtId,corpId){
    orgtIdCheck = orgtId;
    parent.layer.open({
        type: 2,
        area: ['850px', '630px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/addOrgtChildCorp/" + orgtId+"/"+corpId,
        end: function () {
            //bt.datatable.refreshCurrentPage();
            loadOrgtCorpTree(orgtId,'');
        }
    });
}

//查看
function viewCorp(id,corpId,parentId){
    parent.layer.open({
        type: 2,
        area: ['850px', '700px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/view?id="+id+"&corpId="+corpId+"&parentId="+parentId,
        end: function () {
            //bt.datatable.refreshCurrentPage();
           // loadOrgtCorpTree(orgtId);
        }
    });
}

//删除单位
function deleteCorp(id,orgtId){
    if(!id){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    orgtIdCheck =orgtId;
    layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        bt.loading.show();
        $.post(basePath + "/sys/organtype/organtype/deleteCorp",{"id":id,"orgtId":orgtId}, function(result){
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                loadOrgtCorpTree(orgtId,'');
                //organtypeDataTable.draw();
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
            }
            bt.loading.hide();
        },'JSON');
    });
}

//编辑上下级
function updateOrgtSupCorp(orgtId,corpId){

    parent.layer.open({
        type: 2,
        area: ['850px', '630px'],
        maxmin: true,
        content: basePath + "/sys/organtype/organtype/updateOrgtSupCorp/" + orgtId+"/"+corpId,
        end: function () {
            //bt.datatable.refreshCurrentPage();
            orgtIdCheck =orgtId;
            loadOrgtCorpTree(orgtId,'');

        }
    });
}

//设置树形表格高度，可滚动样式
function tableHeader() {
    $("#corpTable").parent().prepend(
        '<table id="corpTable_" class="layui-table" ><thead>' + $("#corpTable thead").html() + '</thead></table>'
    ).css({
        'position': 'relative',
        'height': '362px',
        'overflow-y': 'hidden'
    })
    $("#corpTable_").find('th').each(function(i) {
        $(this).css('width', $('#corpTable').find('th:eq(' + i + ')').width());
    });
    $("#corpTable_").css({
        'position': 'absolute',
        'top': '0',
        'left': 0,
        'z-index': 2
    })
    $("#corpTable").css({
        'position': 'absolute',
        'top': 0,
        'left': 0
    })
    $('.tableBox').scroll(function() {
        if ($('.tableBox').scrollTop() > 0) {
            $("#corpTable_").css('top', $('.tableBox').scrollTop());
        } else {
            $("#corpTable_").css('top', 0);
        }
    });
}

//导出
function excelCorp(){

    // 判断表格是否有数据
    if($('#corpTable tbody tr').length < 1){
        layer.alert(bt.lang('common.msg.temporarily_no_data'), {icon: 7});
        return false;
    }
    var data=bt.datatable.getSelectData('#organtypeTb');
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return false;
    }
    bt.loading.show();
    var orgtId = data[0].id;

    var columns=new Array();

    for(var i =0;i<$('#corpTable thead tr th').length-1;i++){
        columns[i]=$('#corpTable thead tr th')[i].innerHTML;
    }

    var exportUrl =  basePath + "/sys/organtype/organtype/excelCorp";
    var form = $("<form></form>").attr("style", "display:none").attr("action", exportUrl).attr("method", "post");
    try {
        form.append($("<input></input>").attr("type", "hidden").attr("name", "columns").attr("value", columns));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "orgtId").attr("value", orgtId));
        //form.append($("<input></input>").attr("type", "hidden").attr("name", "validSign").attr("value", dicts.get("validSign").val()));
        form.append($("<input></input>").attr("type", "hidden").attr("name", "fileName").attr("value", bt.navtab.getCurrentDom().name));
        form.appendTo('body');
        form.submit();
    } catch (e) {
    } finally {
        form.remove();
        bt.loading.hide();
    }
}


