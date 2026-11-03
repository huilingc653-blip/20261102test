var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框
   
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
            'url': basePath+"/letter/guarantee/guaGuaranteeInfo/loadAllDatatable"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
			{data:"guaranteeNo",className:'center',bSortable:true},
			{data:"corpName",className:'center',bSortable:true,},
			{data:"guaranteeType",bSortable:true,className:'center',
			      render: function ( data, type, full, meta ) {
			      return bt.dict("guarantee.Lg.TypeAttr",full.guaranteeType);
			    }
			 },
			 {data:"openCorpName",className:'center',bSortable:true},
			 {data:"incomeCorp",className:'center',bSortable:true},
			 {data:"guaranteeMoney",bSortable:true,className:'textRight',
			     	  render: function ( data, type, full, meta ) {
			     		  return bt.money.format(full.guaranteeMoney);
			    }
			 },
			 {data:"startDate",className:'center',bSortable:true,
                 render: function ( data, type, full, meta ) {
                     return bt.date.format(full.startDate, "yyyy-MM-dd");
                 }},
			 {data:"endDate",className:'center',bSortable:true,
                 render: function ( data, type, full, meta ) {
                     if(full.endDate!=null){
                         return bt.date.format(full.endDate, "yyyy-MM-dd");
                     }else{
                         return "";
                     }
                 }},
            {data:"guaItem",className:'center',bSortable:false},
            {data:"contractNo",className:'center',bSortable:false},
            {data:"bondMoney",className:'textRight',bSortable:true,
				 render: function ( data, type, full, meta ) {
		     		  return bt.money.format(full.bondMoney);
				 }
			},
            {data:"bondAcc",className:'center',bSortable:false},
            {data:"status",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
				    return bt.dict("guaGuarantee.status",full.status);
				}
			},
            {data:"handleCharge",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.money.format(full.handleCharge);
                    }
                    return '';
                }
            },
            {data:"receiver",className:'center',bSortable:true},
            {data:"receiveDate",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.receiveDate!=null){
                        return bt.date.format(full.receiveDate, "yyyy-MM-dd");
                    }else{
                        return "";
                    }
                }
                },
             {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                 var html ='';
                 //if(bt.isResource('gua:guarantee:info:query',full.corpId)){//查看
                	 html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
              	 //}
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
                    colNum: '6', //汇总列序号
                    dataName: 'guaranteeMoney' //汇总列保函金额
                },
                {
                    colNum: '11', //汇总列序号
                    dataName: 'bondMoney' //汇总列保证金金额
                },
                {
                    colNum: '14', //汇总列序号
                    dataName: 'handleCharge' //汇总列手续费金额
                }
            ]
        }
    });
});

//查看保函申请详情
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
	    if(bt.navtab){
	        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/letter/guarantee/guaGuaranteeInfo/viewDtl/" +data.id);
	        return;
	    }
	}
}