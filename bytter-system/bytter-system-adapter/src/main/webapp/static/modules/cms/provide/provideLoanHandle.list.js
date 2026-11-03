bt.use(['form','datatable','dateType','dicts','corp','select','moneyType','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    //时间
    bt.dateType.init();
    bt.selectType.init();
    //网点/单位
    bt.corp.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        ordering:false,
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
        ajax: {
            'url': basePath+"/cms/provide/provideloandeferred/dataTablehandle"
        },
        searchForm:{
        	formElem:"#searchForm",
            formFilter:"search"
        },
        columns: [
            {data: "contractCode", bSortable: true},
            {data: "netName", bSortable: true},
            /*{data:"loanAcc",bSortable:true},
            {data:"corpName",bSortable:true},*/
            {data: "curName", bSortable: true},
            {data: "bankName", bSortable: true},
            {
                data: "plMoney", bSortable: true, className: "textRight", render: function (data, type, full, meta) {
                    var plMoney = bt.money.format(full.plMoney);
                    return plMoney;
                }
            },
            {
                data: "money", bSortable: true, className: "textRight", render: function (data, type, full, meta) {
                    var money = bt.money.format(full.money);
                    return money;
                }
            },
            {
                data: "plTerm", bSortable: true, render: function (data, type, full, meta) {
                    var term = full.plTerm + (full.ternUnit == "M" ? bt.lang("table.dailyAccount.unitNumber") : "") + bt.dict('PrecativeLoanInfo.ternUnit',full.ternUnit);
                    return term;
                }
            },
            {data: "plDate", bSortable: true},
            {data: "repayDate", bSortable: true},
            {data: "deferredRepayDate", bSortable: true},
            {
                data: "deferredLimt", bSortable: true, render: function (data, type, full, meta) {
                    var term = full.deferredLimt + (full.ternUnit == "M" ? bt.lang("table.dailyAccount.unitNumber") : "") + bt.dict('PrecativeLoanInfo.ternUnit',full.ternUnit);
                    return term;
                }
            },
            {data: "createName", bSortable: true},
            {
                data: "agencyDate", bSortable: true, render: function (data, type, full, meta) {
                    return bt.date.format(full.agencyDate, "yyyy-MM-dd");
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('cms:provide:deferred:handle:edit')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="Application(this)">' + bt.lang("common.button.deferred") + '</a>';
                    }
                    html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="' + meta.row + '" onclick="listMsg(this)">' + bt.lang("common.button.logInfo") + '</a>';
                    // if(full.deferredId != null && full.deferredId != ""){
                    // 	 html += '\<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.deferredId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    //   }
                    return html;
                }
            }
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '5', //汇总列序号
                    dataName: 'plMoney' //汇总列借款金额
                },
                {
                    colNum: '6', //汇总列序号
                    dataName: 'money' //汇总列发放金额
                }
            ]
        }
    });

    $.each($("div[type='list']"),function(i,item){
    	bt.select.init({
    		elem:"[field='"+$(item).attr("field")+"']",
    		name:$(item).attr("field"),
    		defaultVal:$(item).attr("defaultVal"),
    		valueField:$(item).attr("dictField"),
    		textField:$(item).attr("dictText"),
    		placeholder:{value:"",text:""},
    		search:true,
    		disabled:false,
    		url:$(item).attr("fieldhref")

    	});
    });

});
function Application(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();

    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.deferred.handle'),basePath + "/cms/provide/provideloandeferred/defhandle?provideCode=" + data.provideCode+"&deferredId="+data.deferredId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/provide/provideloandeferred/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }


}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.deferredCode);

        }
	}
}
