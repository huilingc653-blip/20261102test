var datatable,dicts;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType'], function(){
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
        // colReorder: true,
        checkbox:true,
        // order: [[7,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/guarantee/pledgeChange/datalist"
        },
        buttons: [
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
                  {data:"updateDate",bSortable:true,className:'center'},
                  {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='';
                   if(bt.isResource('gua:pledge:change:apply',full.corpId) && full.status!=11){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="change(this)">'+bt.lang("common.button.change")+'</a><span class="text-explode"></span>';
                    }
                    if(bt.isResource('gua:pledge:change:query',full.corpId)){//查看和日志权限权限
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

//变更申请
function change(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.change'),basePath + "/cms/guarantee/pledgeChange/addChange/"+data.id);
        return;
    }
}

//查看信息
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/cms/guarantee/pledgeChange/viewchanged/"+data.id);
        return;
    }
}
//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
	    if(bt.navtab){
	         bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/guarantee/pledgeChange/changedlist/"+data.id);
	         return;
	     }
	  }
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
