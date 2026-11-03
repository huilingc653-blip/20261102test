bt.use(['form','datatable','moneyType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [1,'desc'],
        checkbox: true,
        ajax: {
            'url': dataUrl
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                hide: bt.isnotResource('cms:loan:cmsloaninfo:edit'),
                text: bt.lang('批量还款申请'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    loanRepayBatch(data);
                }
            }, {
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
            {data:"netName",className:'center',bSortable:false},
            {data:"loanCode",className:'center',bSortable:true},
            {data:"billCode",className:'center',bSortable:true},
            {data:"loanAcc",className:'center',bSortable:false},
            {data:"createDate",className:'center',bSortable:true},
            {data:"curName",className:'center',bSortable:false},
            {data:"loanMoney",className:'textRight',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.loanMoney);
                }
            },
            {data:"remainMoney",className:'textRight',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return moneyFun.format(moneyFun.sub(full.loanMoney,full.repayedMoney));
                }
            },
            {data:"loanDate",className:'center',bSortable:true},
            {data:"repayDate",className:'center',bSortable:true},
            {data:"deferredReplayDate",className:'center',bSortable:false},
            {data:"repayTypeName",className:'center',bSortable:false},
            {data:"loanClass",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.LoanClass",full.loanClass);
                }
            },
            /*{data:"loanType",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("Cms.PlType.A",full.loanType);
                }
            },*/
            {data:"loanTerm",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
            		if(full.ternUnit==2){
            			return full.loanTerm + bt.lang("cms.loan.ternUnit.day");
            		}else if(full.ternUnit==1){
            			return full.loanTerm + bt.lang("cms.loan.ternUnit.month");
            		}else{
            			return full.loanTerm;
            		}
                }
            },
            {
                data: "prediceRepayDate", className: 'center', bSortable: false,
                render: function (data, type, full, meta) {
                    if (full.repaymentScheduleVO === null) {
                        return '';
                    } else {
                        return full.repaymentScheduleVO.prediceRepayDate;
                    }
                }
            },
            {data:"repaymentMoney",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (full.repaymentScheduleVO === null) {
                        return '';
                    } else {
                        return moneyFun.format(full.repaymentScheduleVO.repaymentMoney);
                    }
                }
            },
            {data:"repaymentIst",className:'textRight',bSortable:false,
                render: function ( data, type, full, meta ) {
                    if (full.repaymentScheduleVO === null) {
                        return '';
                    } else {
                        return moneyFun.format(full.repaymentScheduleVO.repaymentIst);
                    }
                }
            },
            {data:"bizStatus",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.bizStatus",full.bizStatus);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var	html='';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="interestView(this)">' + bt.lang("table.fcAgreedDeposit.interestInfo") + '</a>';
                    if(repayPlan=="1"){
                        html += '<span class="text-explode">|</span>';
                        html += ' <a class="" href="#" data-row="'+meta.row+'" onclick="editRepayPlan(this)">'+bt.lang("cms.loan.RepaymentPlanModify")+'</a>';
            		html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>'
               	 + '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                   return html;
            	}
            	if(bt.isResource('cms:loan:cmsloanrepay:edit',full.corpId)){
	            	if(full.repayingCount==0){
	            		html += ' <a class="" href="#" data-row="'+meta.row+'" onclick="addLoanRepay(this)">'+bt.lang("cms.loanRepay.addButton")+'</a>';
	            	}
            	}

            	html+='<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>'
            	 + '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            outerData: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8', //汇总列序号
                    dataName: 'loanMoney' //汇总列发放金额
                },
                {
                    colNum: '9', //汇总列序号
                    dataName: 'remainMoney' //汇总列贷款余额
                }
            ]
        }
    });
});
//批量还款
function loanRepayBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }


    var cmsLoanRepay = {};
    var cmsLoanRepayList = [];
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        cmsLoanRepay = {
            repaymentScheduleId: rowData[i].repaymentScheduleVO.id,
            corpId: rowData[i].corpId,
            contractCode:rowData[i].contractCode,
            sendCode: rowData[i].billCode,
            repayMoney: rowData[i].repaymentScheduleVO.repaymentMoney,
            repayDate: rowData[i].repaymentScheduleVO.prediceRepayDate,   //还款日期
            repayway: rowData[i].repayType,  //还款方式
            repayType: "2",// 还款类型  归还本金及归还金额利息
            status: "1",
        };
        cmsLoanRepayList.push(cmsLoanRepay);
    }

    bt.loading.show();//显示加载中
    parent.layer.confirm(bt.lang('确定将所选贷款最近一期未还的还款计划进行归还吗？'), function () {
        bt.jquery.ajax({
            type: 'post',
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify(cmsLoanRepayList),
            asycn: false,
            url: basePath + "/cms/loan/cmsloanrepay/batchSubmit",
            success: function (result) {
                bt.loading.hide(); //关闭加载中
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            dataType: 'json'
        });
    });
    bt.loading.hide();
}

//计息信息
function interestView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        var url = "";
        if (data.billCode != null) {
            url = basePath + "/cms/loan/cmsloaninfo/interestView?loanAcc=" + data.loanAcc
        }
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), url);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: url
        });
    }
}
//查看
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/loan/cmsloansend/view/" + data.id);
            return;
        }
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '530px'],
	        maxmin: true,
	        content: basePath + "/cms/loan/cmsloaninfo/view/" + data.id
	    });
    }
}
//还款申请
function addLoanRepay(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loanRepay.addButton'),basePath + "/cms/loan/cmsloanrepay/add?sendCode=" + data.billCode);
            return;
        }
    }
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);
            return;
        }
	}
}

function editRepayPlan(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	var params;
	if(isNull(data.deferredReplayDate)){
		params="billCode=" + data.billCode+"&type=1&rateBase=360";
	}else{
		params="billCode=" + data.billCode+"&type=1" + "&deferredRepayDate="+data.deferredReplayDate + "&zqRate="+ data.deferredRate+"&rateBase=360";
	}

	var path=basePath + "/cms/basedata/updaterepaymentschedule/toRepayPlanUpdate?"+ params;
	if(data){
		if(bt.navtab){
	        bt.navtab.addNavtab(bt.lang('common.button.add'),path);
	        return;
	    }
	    parent.layer.open({
	        type: 2,
	        area: ['850px', '530px'],
	        maxmin: true,
	        content: path,
	        end:function(){
	        	billTable.draw();
	        }
	    });
	}
}
/**
* 判断是否为空 true：空 false:不为空
* @param data
*/
function isNull(data){
return (data == "" || data == undefined || data == null) ? true : false;
}
