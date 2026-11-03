var datatable;
var idsValArray=[];
bt.use(['form','datatable','moneyType','dateType','corp','select','dicts','money','rateType','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.rateType.init();
    bt.mulselect.init();
    dateFmt = bt.date;

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbilldiscountransom/datatable"
        },
        buttons: [
          {
                text: bt.lang('bill.accebill.update.ransom'),
                hide:bt.isnotResource('bill:accebill:abBillDiscountAccount:apply'),
                action: function ( e, dt, node, config ) {
                	var data = dt.rows('.selected').data();
                	submitBatch(data);
                }
            },
            /*{
                text: bt.lang('common.button.download'),
                action: function ( e, dt, node, config ) {
                }
            },*/
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data:"payer",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billMoney",bSortable:true,className:"textRight",
            	render:function(data, type, full, meta){
            		return bt.money.format(full.billMoney);
            	}
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"rpdopendt",bSortable:true,className: "center",
                render : function( data , type , full , meta ){
                    return dateFmt.format(data, "yyyy-mm-dd");
                }
            },
            {data:"rpdduedt",bSortable:true,className: "center",
                render : function( data , type , full , meta ){
                    return dateFmt.format(data, "yyyy-mm-dd");
                }
            },
            {data:"interestRates",bSortable:true,className:"textRight",
            	render : function( data , type , full , meta ){
                    return bt.rateType.getFun().format(full.interestRates,'percent',4,',');
                }
            },
            {data:"interest",bSortable:true,className:"textRight",
            	render:function(data, type, full, meta){
            		return bt.money.format(full.interest);
            	}
            },
            {data:"returnInterest",bSortable:true,className:"textRight",
                render:function(data, type, full, meta) {
                    if (full.status == 11) {
                        return bt.money.format(full.returnInterest);
                    }
                    return '<div id="returnInterest' + full.aid + '" name="returnInterest">0</div>';
                }
            },
            {data:"frontHand",bSortable:true},
            {data:"frontHandAcc",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"enterStockName",bSortable:true},
            {data:"newStmIssuofpprIndName",bSortable:true},
            {data:"blgrdbagcrclPrmtIndName",bSortable:true},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return full.blStdAmt;
                    }else {
                        return "";
                    }
                }
            },
            {data:"blRmtrUnnIdNo",bSortable:true},
            {data:"interest",bSortable:true,className:"textRight",
                render:function(data, type, full, meta){
                    return bt.money.format(full.interest);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
               // html+='<span class="text-explode">|</span>';
                // html += '<a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.aid+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                return html;
            }}
        ],
        /*fnDrawCallback:function(){
            idsValArray=new Array();
            setSumMoney();
        },*/
        btClickRowCallback:function(data){
            var interest,//贴现利息
                buyerinterestMoney,//买方付息
                rpdopendt,//赎回开放日
                rpdduedt,//赎回截止日
                businessDate = $("#businessDate").val(),//业务办理日期
                daynum,//贴现天数
                returnDayNum, //贴现赎回天数
                returnInterest;//返还利息
            //单选
            if(data.length){
                for(var i=0;i<data.length;i++){
                    if(data[i].isSelect){
                        //选中状态，通过业务日期计算返还利息
                        interest = data[i].interest;
                        buyerinterestMoney = data[i].buyerinterestMoney;
                        rpdopendt = data[i].rpdopendt;
                        rpdduedt = data[i].rpdduedt;
                        daynum = data[i].daynum;
                        billMoney = data[i].billMoney;
                        endDate = data[i].endDate;
                        rpdintrstrate = data[i].rpdintrstrate;
                        if(businessDate<rpdopendt||businessDate>rpdduedt){
                            //您选择的第{0}的业务办理日期不在赎回开放日和赎回截止日之间!
                            layer.alert(bt.lang('common.message.abBillDiscountRansom.businessDateCheck',[(i+1)]), {icon: 7});
                            datatable.draw();
                            return;
                        }
                        //返还利息 = (贴现利息-买方付息)*(贴现赎回截止日-业务办理日期)/贴现天数
                        // returnInterest = bt.moneyType.getFun().div(bt.moneyType.getFun().mul(bt.moneyType.getFun().sub(interest, buyerinterestMoney),GetDateDiff(rpdduedt,businessDate)),daynum);
                        // returnInterest = bt.moneyType.getFun().format(returnInterest,{separator: ",",decimal: 2});
                        //returnInterest = returnInterest.toFixed(2);//四舍五入保留2位小数
                        //$("#returnInterest"+data[i].aid).text(returnInterest);
                        //返还利息 = (票据到期日期（结束日期非工作日顺延下一个工作日）-赎回办理日期)*票面金额*赎回利率/100 modify zouxs 单号：B20240100014；2024.01.05
                        var deferdate = culDeferdate(endDate);  //顺延天数
                        if (deferdate == -1){
                            deferdate = 0;
                        }
                        if (rpdintrstrate == null || rpdintrstrate == ""){
                            $("#returnInterest" + data[i].aid).text("0.00");
                        }else {
                            var number = GetDateDiff(endDate, businessDate);//(票据到期日期-赎回办理日期)
                            returnDayNum = deferdate + number;
                            returnInterest = (bt.moneyType.getFun().format((returnDayNum * billMoney * parseFloat(rpdintrstrate) / 360), {decimal: 2})).replace(/,/g, "");
                            $("#returnInterest" + data[i].aid).text(returnInterest);
                        }
                    }else{
                        //反选，返还利息为0
                        $("#returnInterest"+data[i].aid).text(0);
                    }
                }
            }else if(data.data){
                //全选
                if(data.data.length){
                    for(var i=0;i<data.data.length;i++){
                        //选中状态，通过业务日期计算返还利息
                        interest = data.data[i].interest;
                        buyerinterestMoney = data.data[i].buyerinterestMoney;
                        rpdopendt = data.data[i].rpdopendt;
                        rpdduedt = data.data[i].rpdduedt;
                        daynum = data.data[i].daynum;
                        billMoney = data.data[i].billMoney;
                        endDate = data.data[i].endDate;
                        rpdintrstrate = data.data[i].rpdintrstrate;
                        if(businessDate<rpdopendt||businessDate>rpdduedt){
                            layer.alert(bt.lang('common.message.abBillDiscountRansom.businessDateCheck',[(i+1)]), {icon: 7});
                            datatable.draw();
                            return;
                        }
                        //返还利息 = (贴现利息-买方付息)*(贴现赎回截止日-业务办理日期)/贴现天数
                        // returnInterest = bt.moneyType.getFun().div(bt.moneyType.getFun().mul(bt.moneyType.getFun().sub(interest, buyerinterestMoney),GetDateDiff(rpdduedt,businessDate)),daynum);
                        // returnInterest = bt.moneyType.getFun().format(returnInterest,{separator: ",",decimal: 2});
                        // $("#returnInterest"+data.data[i].aid).text(returnInterest);

                        //返还利息 = (票据到期日期（结束日期非工作日顺延下一个工作日）-赎回办理日期)*票面金额*赎回利率/100 modify zouxs 单号：B20240100014；2024.01.05
                        var deferdate = culDeferdate(endDate);  //顺延天数
                        if (deferdate == -1){
                            deferdate = 0;
                        }
                        if (rpdintrstrate == null || rpdintrstrate == ""){
                            $("#returnInterest" + data[i].aid).text("0.00");
                        }else {
                            var number = GetDateDiff(endDate, businessDate);//(票据到期日期-赎回办理日期)
                            returnDayNum = deferdate + number;
                            returnInterest = (bt.moneyType.getFun().format((returnDayNum * billMoney * parseFloat(rpdintrstrate) / 360), {decimal: 2})).replace(/,/g, "");
                            $("#returnInterest" + data[i].aid).text(returnInterest);
                        }
                    }
                }
            }else{
                //全反选，重新加载表格
                datatable.draw();
            }
            opIdsValArray(data);
        },
        btDrawCallback:function(setting){
            idsValArray = []
            setSumMoney();
        },
        fixedColumns:{
        	rightColumns:1
        },
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'10',
                    dataName:'billMoney'
                },
                {
                    colNum:'16',
                    dataName:'interest'
                },
                {
                    colNum:'17',
                    dataName:'returnInterest'
                }
            ]
        }
    });
});

function dateCallBack(){
    datatable.draw();
}

//js计算日期之间天数
function GetDateDiff(startDate,endDate)
{
    // var startTime = new Date(Date.parse(startDate.replace(/-/g,   "/"))).getTime();
    // var endTime = new Date(Date.parse(endDate.replace(/-/g,   "/"))).getTime();
    var startTime = startDate;
    var endTime = endDate
    if(typeof startDate === 'string'){
        startTime = new Date(Date.parse(startDate.replace(/-/g, "/"))).getTime();
    }

    if(typeof endDate === 'string'){
        endTime = new Date(Date.parse(endDate.replace(/-/g, "/"))).getTime();
    }
    var dates = Math.abs((startTime - endTime))/(1000*60*60*24);
    return  dates;
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view') ,
        		basePath + "/bill/accebill/abreceivebill/view?vouId="+data.vid+"&viceId="+data.vvid+"&accId="+data.aid);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId="+data.vid+"&viceId="+data.vvid+"&accId="+data.aid
    });
}

//批量提交
function submitBatch(data){
	var $=bt.jquery;len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
    	return;
    }
    var rowData = data.splice(0,len),businessDate = $("[name=businessDate]").val();
    if(!businessDate){
    	//业务归属日期必填
    	layer.alert(bt.lang('accebill.abbilldiscountcollect.businessDateisnull', {icon: 7}));
    	return;
    }
    var rpdopendt,rpdduedt;//赎回开放日，赎回截止日
    for(i=0;i<len;i++){
    	rpdopendt = rowData[i].rpdopendt;
    	rpdduedt = rowData[i].rpdduedt;
    	if(businessDate<rpdopendt||rpdopendt>rpdduedt){
    		//你选择的第{0}条数据 不符合要求:业务归属日期要处于赎回开放日与赎回截止日期间!
    		layer.alert(bt.lang('accebill.abbilldiscountcollect.businessdaterror',[(i+1)]), {icon: 7});
    		return;
    	}
    }
    parent.layer.confirm(bt.lang('common.message.confirm.submit'),function(){
        var vids,vvids,aids,rpdopendt,rpdduedt,returnInterest,returnInterests,interestRates,interestRatesList,rpdopendtAry,rpdduedtAry;
        for(var i=0;i<len;i++){
        	if(vids){
        		vids=vids+","+rowData[i].vid;
        	}else{
        		vids=rowData[i].vid;
        	}
        	if(vvids){
        		vvids=vvids+","+rowData[i].vvid;
        	}else{
        		vvids=rowData[i].vvid;
        	}
        	if(aids){
        		aids=aids+","+rowData[i].aid;
        	}else{
        		aids=rowData[i].aid;
        	}
            rpdopendt = rowData[i].rpdopendt;
            rpdduedt = rowData[i].rpdduedt;
            interestRates = rowData[i].interestRates;
            returnInterest = $("#returnInterest"+rowData[i].aid).text();
        	if(returnInterests){
        		returnInterests = returnInterests+","+returnInterest;//返还利息
        	}else{
        		returnInterests = returnInterest;
        	}
        	if(interestRatesList){
                interestRatesList = interestRatesList+","+interestRates;//贴现利率
            }else{
                interestRatesList = interestRates;
            }
            if(rpdopendtAry){
                rpdopendtAry = rpdopendtAry+","+rpdopendt;//赎回开放日
            }else{
                rpdopendtAry = rpdopendt;
            }
            if(rpdduedtAry){
                rpdduedtAry = rpdduedtAry+","+rpdduedt;//赎回截止日
            }else{
                rpdduedtAry = rpdduedt;
            }
        }
        $.post(basePath + "/bill/accebill/abbilldiscountransom/batchSubmit",{"vids":vids,"vvids":vvids,"aids":aids,
        	"businessDate":businessDate,"returnInterests":returnInterests,"interestRatesList":interestRatesList,
            "rpdopendtAry":rpdopendtAry,"rpdduedtAry":rpdduedtAry}, function(result){
        	parent.layer.alert(result.msg, {icon: result.success?1:2});
        	if(result.success){
    			bt.datatable.refreshCurrentPage();
        	}
        },'JSON');
    });
}

// 计算总条数
var opIdsValArray = function(datas){
    if(datas&&datas.length>0){
        for(i=0;i<datas.length;i++){
            if(datas[i].isSelect){
                if(idsValArray.indexOf(datas[i].vouId)<0){
                    idsValArray.push(datas[i].vouId);
                }
            }else{
                var index=idsValArray.indexOf(datas[i].vouId);
                if(index > -1){
                    objArrayRemove(idsValArray,index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function (){
    var showHTML=bt.lang("sys.common.isSelectSize",[idsValArray.length]);
    if($("#sumMoneyBillSpan").text()!=''){
        $("#sumMoneyBillSpan").text(showHTML);
    }else{
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">'+showHTML+'</span>');
    }
}

var objArrayRemove = function(obj,index){
    if (index > -1) {
        obj.splice(index, 1);
    }
};

//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp",data);
}

//计算假日顺延天数
function culDeferdate(culEndDate) {
    $ = bt.jquery, form = bt.form();
    var deferdate;
    var url = basePath + '/bill/accebill/abBillDiscountManager/getDeferdate';
    $.ajax({
        type: 'post',
        data: {"endTime": culEndDate},
        async: false,
        url: url,
        cache: false,
        success: function (result) {
            if (result.success) {
                bt.use(['date', 'dateType'], function () {
                    var workDay = dateFmt.format(dateFmt.get(result.obj), "yyyy-mm-dd");
                    deferdate = bt.dateType.getFun().getBetweenDay(culEndDate, workDay);
                })
            } else {
                deferdate = -1;      //返回null,返回-1
            }
        },
        dataType: 'json'
    });
    return deferdate;
}
