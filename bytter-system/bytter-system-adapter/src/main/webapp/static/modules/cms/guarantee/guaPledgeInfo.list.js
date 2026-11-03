var datatable,dicts;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType','btImport'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框

  // 下拉框
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

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[7,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/guarantee/pledgeInfo/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('gua:pledge:info:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },
            {
                hide: bt.isnotResource('gua:pledge:info:apply'),//应用权限
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            },
            {
                hide: bt.isnotResource('gua:pledge:info:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
                  {data:"corpName",className:'center',bSortable:true},
                  {data:"pledgeAttr",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guarantee.pledge.TypeAttr",full.pledgeAttr);
                      }
                  },
                  {data:"pledgeNo",className:'center',bSortable:true},
                  {data:"typeName",className:'center',bSortable:true},
                  {data:"pledgeName",className:'center',bSortable:true},
                  {data:"startDate",className:'center',bSortable:true},
                  {data:"endDate",className:'center',bSortable:true},
                  {data:"pledgeMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		 return bt.money.format(full.pledgeMoney);
                      }
                  },
                  {data:"pledgeNetWorth",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                         return bt.money.format(full.pledgeNetWorth);
                      }
                  },
                  {data:"curName",className:'center',bSortable:true},
                  {data:"pledge",className:'center',bSortable:true},
                  {data:"pledgeOrg",className:'center',bSortable:true},
                  {data:"regNumber",className:'center',bSortable:true},
                  {data:"regCorpName",className:'center',bSortable:true},
                  {data:"userName",className:'center'},
                  {data:"status",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guaPledge.status",full.status);
                      }
             },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('gua:pledge:info:apply',full.corpId) && full.status!=11){//更新和删除权限
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode"></span>';
                 }
                if(bt.isResource('gua:pledge:info:query',full.corpId)){//查看和日志权限权限
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
              	}
                  return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8', //汇总列序号
                    dataName: 'pledgeMoney' //汇总列评估值
                }
            ]
        }
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/guarantee/pledgeInfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/pledgeInfo/add",
        end:function(){
        	datatable && datatable.draw();
        }
    });
}

//查看抵质押物信息详情
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/pledgeInfo/view/" +data.id
    });
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/guarantee/pledgeInfo/editPledgeInfo/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/pledgeInfo/editPledgeInfo/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}


//批量提交
function submitBatch(data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    for(var i=0;i<rowData.length;i++){
        if(rowData[i].status!=11)
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
        $.post(basePath + "/cms/guarantee/pledgeInfo/batchSubmit",{ids:ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

//日志列表
function listMsg(el){
  var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
  if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/guarantee/gualog/listlog?guaId=" + data.id);
            return;
        }
  }
}

//删除行
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/guarantee/pledgeInfo/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

/**判断字符串是否为空*/
bt.empty = function(ms){
	if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
		return true;
	}
	return false;
}

//重新加载抵质押性质
bt.refreshPledgeAttr=function(data){
	if(bt.empty(data.typeattr)){
		dicts.get('pledgeAttr').val("");
		var getCurrentSelect = selectTypes.get("pledgeType");
		getCurrentSelect.attr({
			"data-url":basePath+"/cms/guarantee/pledgeType/json",
			"data-defaultvalue":""
		});
		selectTypes.render();
	}else{
		dicts.get('pledgeAttr').val(data.typeattr);
	}
}
//重新加载抵质押物类型
bt.refreshPledgeType=function(data){
	var getCurrentSelect = selectTypes.get("pledgeType");
	if(bt.empty(data.value)){
		getCurrentSelect.attr({
			"data-url":basePath+"/cms/guarantee/pledgeType/json",
			"data-defaultvalue":""
		});
	}else{
	getCurrentSelect.attr({
			"data-url":basePath+"/cms/guarantee/pledgeType/json/"+data.value,
			"data-defaultvalue":""
		});
	}
	selectTypes.render();
}

//点击重置后重新加载抵质押物类型
resetPledgeType = function(){
	var getCurrentSelect = selectTypes.get("pledgeType");
	getCurrentSelect.attr({
		"data-url":basePath+"/cms/guarantee/pledgeType/json",
		"data-defaultvalue":""
	});
	selectTypes.render();
}

/**
 * 导入功能
 */
importExcel = function () {
    up = bt.btImport.init({
        code: "70000061",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/cms/guarantee/pledgeInfo/saveImport",
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(up.getSelectData()),
                success: function (result) {
                    bt.datatable.refreshCurrentPage();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }
            });
        }
    });
};
