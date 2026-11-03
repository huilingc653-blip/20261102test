var datatable,dicts;
var pledgeManyTimes = "0";
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框
    pledgeManyTimes = $("#pledgeManyTimes").val();
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
            'url': basePath+"/cms/guarantee/pledgeInfo/searchtable"
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
                  {data:"corpName",bSortable:true},
                  {data:"pledgeAttr",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guarantee.pledge.TypeAttr",full.pledgeAttr);
                      }
                  },
                  {data:"pledgeNo",bSortable:true},
                  {data:"typeName",bSortable:true},
                  {data:"pledgeName",bSortable:true},
                  {data:"startDate",bSortable:true},
                  {data:"endDate",bSortable:true},
                  {data:"curName",bSortable:true},
                  {data:"pledge",bSortable:true},
                  {data:"pledgeOrg",bSortable:true},
                  {data:"regNumber",bSortable:true},
                  {data:"regCorpName",bSortable:true},
                  {data:"pledgeMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.pledgeMoney);
                      }
                  },
                  {data:"freezeMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.freezeMoney);
                      }
                  },
                  {data:"occupyMoney",bSortable:true,className:'textRight',render: function ( data, type, full, meta ) {
                	  	var html ='';
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="viewOccupy(this)">'+ bt.money.format(full.occupyMoney)+'</a>';
                  	    return html;
                      }
                  },
                  {data:"availMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                          return bt.money.format(data);
                      }
                  },
                {data:"userName"},
                {data:"status",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guaPledge.status",full.status);
                      }
             },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.dtl.view")+'</a>';
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
                    colNum: '10', //汇总列序号
                    dataName: 'pledgeMoney' //汇总列评估值
                },
                {
                    colNum: '11', //汇总列序号
                    dataName: 'freezeMoney' //汇总列冻结抵质押价值
                },
                {
                    colNum: '12', //汇总列序号
                    dataName: 'occupyMoney', //汇总列已抵质押价值
                },
                {
                    colNum: '13', //汇总列序号
                    dataName:'availMoney' //汇总列可用价值
                }
            ]
        }
    });
});

//查看明细
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.dtl.view'), basePath + "/cms/guarantee/pledgeInfo/view/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/pledgeInfo/view/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//查看明细
function viewOccupy(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.dtl.view'), basePath + "/cms/guarantee/pledgeOccupy/viewOcuppyList/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/pledgeOccupy/viewOcuppyList/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
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

