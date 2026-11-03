var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
   
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
            'url': basePath+"/cms/guarantee/pledgeInfo/approveDataList" 
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
                  {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                  	var html='';
                  	if($("#flowStatus").val()=='todo'){
                  		html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
                  	}
                      if($("#flowStatus").val() == 'pass'){
                          html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                          html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                      }else {
                          html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                      }
                      return html;
                  }}
        ],
        summary:{
            pageSum: false,//小计功能关闭
            allSum: true,//汇总功能开启
            selectSum: true,//已选汇总功能开启
            // groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',//汇总列序号
                    dataName: 'pledgeMoney',//汇总列数据
                }
            ]
        }
    });
    
    bt.workFlow.beforeSubmit = function(submitData){   
    	var data = bt.datatable.getSelectData("#appTable");
        if(data.length < 1){
        	layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 2});
        	return;
        }
    	var ids = []; 
    	var taskIds = []; 
    	// 遍历行数据
    	$.each(data,function(i, tr) {
    		taskIds.push(tr.taskId);
    	});
		bt.workFlow.taskList = taskIds;
		return true;
    }
});

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
    	bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/cms/guarantee/pledgeInfo/viewApprove?id=" + data.id + "&taskId=" + data.taskId);
    	return;
    }
    parent.layer.open({
		type: 2,
		area: ['850px', '530px'],
		maxmin: true,
		content: basePath + "/cms/guarantee/pledgeInfo/viewApprove?id=" + data.id + "&taskId=" + data.taskId
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