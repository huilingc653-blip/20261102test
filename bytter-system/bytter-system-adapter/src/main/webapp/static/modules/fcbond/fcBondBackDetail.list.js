var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.bank.init({
        elem:'.bank',
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[6,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bond/fcbondback/datatable" 
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
            {data:"bankAcc",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"bankName",bSortable:true},
            {data:"curName",bSortable:true,className:'center'},
            {data:"bondTypeName",bSortable:true},
            {data:"balMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
            		return bt.money.format(full.balMoney);
                }
            },
            {data:"applyMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
            		return bt.money.format(full.applyMoney);
                }
            },
            {data:"availMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
            		return bt.money.format(full.availMoney);
                }
            },
            {data:"backInterest",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
            		return bt.money.format(full.backInterest);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('netbank:fcBond:depositBack:apply',full.corpId) && ((full.balMoney+full.applyMoney+full.availMoney+full.backInterest)!=0)){
                   html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
                }
                if(bt.isResource('netbank:fcBond:depositBack:query',full.corpId)){
                   html += '<a class="" href="#" data-row="'+meta.row+'" onclick="back(this)">'+bt.lang("fcbond.button.back")+'</a>'; 
                }
                   return html;
            }}
        ],
        summary:{
            pageSum:true,//小计功能开启
            selectSum:true,//已选汇总功能开启
            groupColumn:'curName',//分类汇总列参数
            format:{
                decimal: 2
            },
            sumList:[
                {
                    colNum:'8',//汇总列序号
                    dataName:'balMoney',//汇总列数据
                },
                {
                    colNum:'9',//汇总列序号
                    dataName:'applyMoney',//汇总列数据
                },
                {
                    colNum:'10',//汇总列序号
                    dataName:'availMoney',//汇总列数据
                },
                {
                    colNum:'11',//汇总列序号
                    dataName:'backInterest',//汇总列数据
                },
            ]
        },
    });
});


//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
      	if(bt.navtab){
      	 	bt.navtab.addNavtab(bt.lang('common.button.view'), basePath + "/bond/fcbondback/viewbondback/"+data.bondId);
      		return;
      	}
}

//转回
function back(el){
	  var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	  if (bt.navtab) {
		  bt.navtab.addNavtab(bt.lang('fcbond.button.back'), basePath + "/bond/fcbondback/backdetail/"+data.bondId);
		  return;
	  }
}
