var cntrCount = 0;
var cntrDebMoney = 0;
var cntrCreMoney = 0;
var bankCount = 0;
var bankDebMoney = 0;
var bankCreMoney = 0;
var differenceMoney = 0;
var cntrDifferenceMoney = 0;
var bankDifferenceMoney = 0;
var cntrTbData = new Array();
var bankTbData = new Array();
var CntrCheckDatas;
var BankCheckDatas;
var dateChangeCallback;
var selectTableType, moneyType,dicts,dateTypes;
bt.use(['form','jquery','datatable','bttable','dateType','selectTableType','btdate','corp','selectType','element','money','dicts','moneyType','freezeTable'], function(){
    $ = bt.jquery,form = bt.form();
    moneyType = bt.moneyType.init();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == ""){
            return true;
        }
        return false;
    }
    dateChangeCallback = function () { //
        $getAccBalance($("#account").val(), $("#bankAcc").val(), $("#curId").val());
    }

    var corps = bt.corp.init();
    dicts = bt.dicts.init();
    dateTypes = bt.dateType.init()
    var selectTypes = bt.selectType.init();

    //-----------------------------按钮模块------------------------------------------
    //对账
    form.on('submit(doCheck)', function(data){
        if($doValidate()){
            var action = "/finacial/check/fccompareerp/docheck";
            $.post(basePath + action,data.field,function(result){
                layer.alert(result.msg, {icon: result.success?1:2});
                if(result.success){
                    layer.alert(bt.lang("finacial.check.succeed"), {icon:1});
                    $("#checkFlag").val("1");
                    $reloadTotalInfo();
                }else{
                    layer.alert(bt.lang("finacial.check.fail"), {icon:2});
                }
            },'JSON');
        }
        return false;
    });
    //对账
    form.on('submit(search)', function(data){
        $("#checkFlag").val("2");
        $reloadTotalInfo();
        return false;
    });
    //手工勾兑
    form.on('submit(doHandOver)', function(data){
        $doOverOp("1", "0");
        return false;
    });

    //手工反勾对
    form.on('submit(doReverseHandOver)', function(data){
        $doOverOp("2", "2");
        return false;
    });

    //确认未达
    form.on('submit(doHandStay)', function(data){
        doHandStay();
        return false;
    });

    //结转
    form.on('submit(doAdjustOver)', function(data){
        doAdjustOver(data);
        return false;
    });

    //打印
    form.on('submit(doPrint)', function(data){
        $doPrint(data);
        return false;
    });
    //未勾对记录
    form.on('radio(noHandRadios)', function(data){
        $reloadTotalInfo();
    });
    //未达账记录
    form.on('radio(stayRadios)', function(data){
        $reloadTotalInfo();
    });
    //已经勾对记录
    form.on('radio(handRadios)', function(data){
        $reloadTotalInfo();
    });
    //已经对账记录
    form.on('radio(checkRadios)', function(data){
        $reloadTotalInfo();
    });

    //对账
    form.on('submit(resetSome)', function(data){
        moneyType.get("companyStartMoney").val("");
        moneyType.get("companyEndMoney").val("");
        moneyType.get("bankStartMoney").val("");
        moneyType.get("bankEndMoney").val("");
        dicts.get("cntrLangCode").val("");
        dicts.get("bankLangCode").val("");
        $("#cntrSummaryContent").val("");
        $("#oppAccName").val("");
        dateTypes.get("cntrStartTallyDate").val("");
        dateTypes.get("cntrEndTallyDate").val("");
        dateTypes.get("bankStartTallyDate").val("");
        dateTypes.get("bankEndTallyDate").val("");
        return false;
    });

    //-----------------------------方法模块------------------------------------------
    //重新加载列表数据
    $reloadTotalInfo = function(){
        if("1" == $("#checkFlag").val()){
            // if(CntrCheckDatas == null || CntrCheckDatas == undefined){
            //     loadCntrCheckDatas(getQueryParam("1"));
            //     loadBankCheckDatas(getQueryParam("0"));
            // }else{//重新渲染
            //     CntrCheckDatas.config.urldata = getQueryParam("1");
            //     CntrCheckDatas.reload();
            //     BankCheckDatas.config.urldata = getQueryParam("0");
            //     BankCheckDatas.reload();
            // }
            getCntrTbTableData(getQueryParam("1"));
            getBankTbTableData(getQueryParam("0"));
            calAdjustBal();//计算余额调节表数据
        }else if("2"== $("#checkFlag").val()){
            getCntrTbTableData(getQueryParam("1"));
            getBankTbTableData(getQueryParam("0"));
        }
    }

    function getQueryParam(queryType){
        var queryParam = {};
        queryParam = { "queryType":queryType,"account" : $("#account").val(),
            "bankAcc" : $("#bankAcc").val(), "transDate" : dateTypes.get("transTime").val(),
            "checkFlag" : $("#checkFlag").val(), "curId" : $("#curId").val()}
        var noHandRadios = $('input[name="noHandRadios"]:checked').val();
        var stayRadios = $('input[name="stayRadios"]:checked').val();
        var handRadios = $('input[name="handRadios"]:checked').val();
        var checkRadios = $('input[name="checkRadios"]:checked').val();
        if (1 == queryType) {
            var startMoney = moneyType.get("companyStartMoney").val();
            var endMoney = moneyType.get("companyEndMoney").val();
            var cdSign = dicts.get("cntrLangCode").val();
            var summaryContent = $("#cntrSummaryContent").val();
            var startTallyDate = dateTypes.get("cntrStartTallyDate").val();
            var endTallyDate = dateTypes.get("cntrEndTallyDate").val();
            queryParam.startMoney = startMoney;
            queryParam.endMoney = endMoney;
            queryParam.cdSign = cdSign;
            queryParam.summaryContent = summaryContent;
            queryParam.startTallyDate = startTallyDate;
            queryParam.endTallyDate = endTallyDate;
        } else {
            var startMoney = moneyType.get("bankStartMoney").val();
            var endMoney = moneyType.get("bankEndMoney").val();
            var cdSign = dicts.get("bankLangCode").val();
            var oppAccName = $("#oppAccName").val();
            var startTallyDate = dateTypes.get("bankStartTallyDate").val();
            var endTallyDate = dateTypes.get("bankEndTallyDate").val();
            queryParam.startMoney = startMoney;
            queryParam.endMoney = endMoney;
            queryParam.cdSign = cdSign;
            queryParam.summaryContent = summaryContent;
            queryParam.oppAccName = oppAccName;
            queryParam.startTallyDate = startTallyDate;
            queryParam.endTallyDate = endTallyDate;
        }
        queryParam.noHandRadios = noHandRadios;
        queryParam.stayRadios = stayRadios;
        queryParam.handRadios = handRadios;
        queryParam.checkRadios = checkRadios;
        return queryParam;
    }

    // 第一个表格 start
    var CntrTbColumns = [
        {data:"tallyDate",className:'center',text:"日期",bSortable: true,render:function(data,id){
                return bt.date.format(data.tallyDate,"yyyy-mm-dd");;
            }
        },
        {
            data: "cdSign", className: 'center',text:"借/贷", bSortable: true, render: function (data, id) {
                if (data.cdSign == "1") {
                    return bt.lang("finacial.check.cdSign.debit");//借
                } else {
                    return bt.lang("finacial.check.cdSign.credit");//贷
                }
            }
        },
        {
            data: "money", className: 'textRight',text:"金额", bSortable: true,
            render: function (data, id) {
                return bt.money.format(data.money);
            }
        },{
            data: "summaryContent", text:"摘要",bSortable: true, render: function (data, id) {
                var html = "";
                if (data.summaryContent != null) {
                    html = '<div title="' + data.summaryContent + '" class="keep-all" style="max-width:300px">' + data.summaryContent + '</div>';
                }
                return html;
            }
        },
        {
            data: "voucherNo", bSortable: true,
            text:"凭证号",
            render: function (data, id) {
                if (data.voucherNo == null || data.voucherNo == undefined) {
                    return "";
                } else {
                    return data.voucherNo;
                }
            }
        }
    ]
    var cntrTableData = []; // 缓存表格数据
    // 获取第一个表格数据
    function getCntrTbTableData(queryParam) {
        $.ajax({
            type: "POST",
            url: basePath + "/finacial/check/fccompareerp/toLoadCheckDatas",
            data: JSON.stringify(queryParam),
            dataType: 'json',
            contentType: "application/json",
            success: function (data, status, xhr) {
                cntrTableData = data;
                renderCntrTbTable(data,queryParam);
                calCheckTotalData("cntrTb", data);
                $("#cntrTb tr").each(function (index, eml) {//初始化表格背景色
                    if (index > 0) {
                        if (data[index - 1].compareSign == "1") {//已对账
                            eml.style.backgroundColor = "#4FBAE6";
                        } else if (data[index - 1].compareSign == "2") {//手工勾兑
                            eml.style.backgroundColor = "#B0DC81";
                        } else if (data[index - 1].compareSign == "4") {//本期未达
                            eml.style.backgroundColor = "#FF8080";
                        } else {//未对账
                            eml.style.backgroundColor = "#FFFF80";
                        }
                    }
                });

                bt.freezeTable.init({
                    elem:'#cntrTb',//表格id
                    freezeRowNum:'1',//冻结几行
                    freezeColumnNum:'0',//冻结几列
                })
            },
        });
    }
    // 绘制第一个表格
    function renderCntrTbTable(data,queryParam) {
        // sorting_desc  sorting_asc sorting
        var html = '<div style="width:100%;height:370px;overflow:auto;"><table id="cntrTb" class="bttable sorting-table" style="width:100%">'
        html += '<thead>'+createHeadHtml(CntrTbColumns,queryParam)+'</thead>'
        html += '<tbody>'+createBodyHtml(CntrTbColumns,data)+'</tbody>'
        html += '</table></div>'
        $('#sortingTalbe1').html(html);
    }

    // 第一个表格排序事件
    $('#sortingTalbe1').on('click','th',function () {
        // 如果有col-sorting标识，说明可以排序
        if($(this).hasClass('col-sorting')){
            var colName = $(this).data("name");
            // sorting_desc 降序  sorting_asc 升序 sorting
            var sortingType = "";
            if($(this).hasClass("sorting")){
                sortingType = "asc" // 升序
                $(this).removeClass("sorting");
                $(this).addClass("sorting_asc");
            }else if ($(this).hasClass("sorting_desc")){
                sortingType = "asc" // 升序
                $(this).removeClass("sorting_desc");
                $(this).addClass("sorting_asc");
            }else{
                sortingType = "desc" // 降序
                $(this).removeClass("sorting_asc");
                $(this).addClass("sorting_desc");
            }
            sortCntrTable({colName:colName,type:sortingType})
        }
    })
    // 第一个表格全选事件事件
    $('#sortingTalbe1').on('click','.table-checkall',function () {
       var isChecked = $(this).prop('checked');
       $('#sortingTalbe1').find('.table-checkchild').prop('checked',isChecked)
        updateTableDataStauts('checkall',isChecked,'cntrTb');

    })

    // 第一个表格表体中复选框选择事件
    $('#sortingTalbe1').on('click','.table-checkchild',function () {
        var isChecked = $(this).prop('checked');
        updateCheckAll('#sortingTalbe1')
        updateTableDataStauts($(this).data('id'),isChecked,'cntrTb');
    })

    // 第一个表格排序
    function sortCntrTable(sorting) {
        var queryParam = getQueryParam("1");
        queryParam.orderKey = sorting.colName;
        queryParam.orderType = sorting.type;
        getCntrTbTableData(queryParam)
    }
    // 第一个表格 end

    // 第二个表格 start
    var BankTbColumns = [
        {data:"tallyDate",className:'center',text:'日期',bSortable:true,render:function(data,id){
                return bt.date.format(data.tallyDate,"yyyy-mm-dd");
            }
        },
        // {data:"billNo",render:function(data,id){
        //     if(data.billNo==null){
        //         return '';
        //     }else{
        //         return data.billNo;
        //     }
        // }},
        {data:"cdSign",className:'center',text:'借/贷',bSortable:true,render:function(data,id){
                if(data.cdSign=="1"){
                    return bt.lang("finacial.check.cdSign.debit");
                }else{
                    return bt.lang("finacial.check.cdSign.credit");
                }
            }},
        {data:"money",className:'textRight',text:'金额',bSortable:true,
            render:function(data,id){
                return bt.money.format(data.money);
            }
        },
        {data:"oppAccName",text:'对方账户名称',bSortable:true,render:function(data,id){
                var html = "";
                if (data.oppAccName != null) {
                    html = '<div title="' + data.oppAccName + '" class="keep-all" style="max-width: 300px">' + data.oppAccName + '</div>';
                }
                return html;
            }},
        {data:"voucherNo",text:'凭证号',bSortable:true,
            render:function(data,id){
                if(data.voucherNo == null || data.voucherNo == undefined){
                    return "";
                }else{
                    return data.voucherNo;
                }
            }
        }
    ]
    var bankTableData = []; // 缓存表格数据
    // 获取第二个表格数据
    function getBankTbTableData(queryParam) {
        $.ajax({
            type: "POST",
            url: basePath + "/finacial/check/fccompareerp/toLoadCheckDatas",
            data: JSON.stringify(queryParam),
            dataType: 'json',
            contentType: "application/json",
            success: function (data, status, xhr) {
                bankTableData = data;
                renderBankTbTable(data,queryParam);
                calCheckTotalData("bankTb",data);
                $("#bankTb tr").each(function(index,eml){
                    if(index>0){
                        if(data[index-1].compareSign=="1"){//已对账
                            eml.style.backgroundColor="#4FBAE6";
                        }else if(data[index-1].compareSign=="2"){//手工勾兑
                            eml.style.backgroundColor="#B0DC81";
                        }else if(data[index-1].compareSign=="4"){//本期未达
                            eml.style.backgroundColor="#FF8080";
                        }else{//未对账
                            eml.style.backgroundColor="#FFFF80";
                        }
                    }
                });

                bt.freezeTable.init({
                    elem:'#bankTb',//表格id
                    freezeRowNum:'1',//冻结几行
                    freezeColumnNum:'0',//冻结几列
                })
            },
        });
    }
    // 获取第二个表格绘制
    function renderBankTbTable(data,queryParam) {
        // sorting_desc  sorting_asc sorting
        var html = '<div style="width:100%;height:370px;overflow:auto;"><table id="bankTb" class="bttable sorting-table" style="width:100%">'
        html += '<thead>'+createHeadHtml(BankTbColumns,queryParam)+'</thead>'
        html += '<tbody>'+createBodyHtml(BankTbColumns,data)+'</tbody>'
        html += '</table></div>'
        $('#sortingTalbe2').html(html);
    }

    // 第二个表格全选事件事件
    $('#sortingTalbe2').on('click','.table-checkall',function () {
        var isChecked = $(this).prop('checked');
        $('#sortingTalbe2').find('.table-checkchild').prop('checked',isChecked)
        updateTableDataStauts('checkall',isChecked,'bankTb');

    })

    // 第二个表格表体中复选框选择事件
    $('#sortingTalbe2').on('click','.table-checkchild',function () {
        updateCheckAll('#sortingTalbe2')
        updateTableDataStauts($(this).data('id'),$(this).prop('checked'),'bankTb');
    })

    // 第二个表格排序事件
    $('#sortingTalbe2').on("click",'th',function () {
        // 如果有col-sorting标识，说明可以排序
        if($(this).hasClass('col-sorting')){
            var colName = $(this).data("name");
            // sorting_desc 降序  sorting_asc 升序 sorting
            var sortingType = "";
            if($(this).hasClass("sorting")){
                sortingType = "asc" // 升序
                $(this).removeClass("sorting");
                $(this).addClass("sorting_asc");
            }else if ($(this).hasClass("sorting_desc")){
                sortingType = "asc" // 升序
                $(this).removeClass("sorting_desc");
                $(this).addClass("sorting_asc");
            }else{
                sortingType = "desc" // 降序
                $(this).removeClass("sorting_asc");
                $(this).addClass("sorting_desc");
            }
            sortBankTable({type:sortingType,colName:colName})
        }
    })

    // 第二个表格排序
    function sortBankTable(sorting) {
        var queryParam = getQueryParam("0");
        queryParam.orderKey = sorting.colName;
        queryParam.orderType = sorting.type;
        getBankTbTableData(queryParam)
    }
    // 第二个表格 end

    // 生成表头
    function createHeadHtml(columns,queryParam) {
        var html = '<tr><th class="col-check"><input type="checkbox" lay-ignore class="table-checkall"></th>'
        bt.each(columns,function(i,col){
            var className = ''
            if(col.bSortable){
                // sorting_desc  sorting_asc sorting
                className = (queryParam.orderType && queryParam.orderKey === col.data)?"col-sorting sorting_"+queryParam.orderType :"col-sorting sorting"
            }
            html += '<th data-name="'+col.data+'" class="'+className+'">'+col.text+'</th>';
        })
        html += '</tr>'
        return html
    }
    // 生成表体
    function createBodyHtml(columns,data) {
        var html = '';
        bt.each(data,function (j,row) {
            html += '<tr>'
            html += '<td class="col-check"><input type="checkbox" class="table-checkchild" lay-ignore data-id="'+row.id+'"></td>'
            for (var i = 0; i < columns.length; i++) {
                var col = columns[i]
                var className = col.className || ""
                var text = row[col.data]
                if(col.render){
                    text = col.render(row)
                }
                html += '<td class="'+className+'">'+text+'</td>'
            }
            html += '</tr>'
        })
        return html
    }
    // 更新全选功能
    function updateCheckAll(containerId) {
        var allChildCheckboxs = $(containerId).find('.table-checkchild').length;
        var allchildCheckedCheckboxs = $(containerId).find('.table-checkchild:checked').length;
        if(allChildCheckboxs === allchildCheckedCheckboxs){ //如果全部选中了,则全选选中
            $(containerId).find('.table-checkall').prop('checked',true)
        }else{
            $(containerId).find('.table-checkall').prop('checked',false)
        }
    }
    //表格行选中时，更新数据的选中状态
    function updateTableDataStauts(id,isChecked,tableId){
        var data = []
        // tableId 为cntrTb 时代表是第一个表格，bankTb 为第二个表格
        if( tableId === 'cntrTb'){
            data = cntrTableData
        }else{
            data = bankTableData
        }
        // 如果有id代码为单个选中
        if(id){
            bt.each(data,function (i,item) {
                if(id === 'checkall'){ // id为checkall 代码点击全选
                    item.select = isChecked;
                }else if(item.id === id){
                    item.select = isChecked;
                    return false
                }
            })
        }
        calCheckTotalData(tableId, data);
    }

    //加载企业日记账数据
    function loadCntrCheckDatas(queryParam){
        CntrCheckDatas = bt.bttable.init({
            elem: "#cntrTb",
            checkbox: true,
            url: basePath + "/finacial/check/fccompareerp/toLoadCheckDatas",
            urldata: queryParam,
            contentType: "application/json",
            mulselect: true,
            headerCheckbox: true,
            selectCss: false,
            scrollX: true,
            ordering: true,
            scrollY: "370px",
            columns: [
                {data:"tallyDate",className:'center',render:function(data,id){
                        return bt.date.format(data.tallyDate,"yyyy-mm-dd");;
                    }
                },
                {
                    data: "cdSign", className: 'center', bSortable: true, render: function (data, id) {
                        if (data.cdSign == "1") {
                            return bt.lang("finacial.check.cdSign.debit");//借
                        } else {
                            return bt.lang("finacial.check.cdSign.credit");//贷
                        }
                    }
                },
                {
                    data: "money", className: 'textRight', bSortable: true,
                    render: function (data, id) {
                        return bt.money.format(data.money);
                    }
                },{
                    data: "summaryContent", bSortable: true, render: function (data, id) {
                        var html = "";
                        if (data.summaryContent != null) {
                            html = '<div title="' + data.summaryContent + '" class="keep-all" style="max-width:300px">' + data.summaryContent + '</div>';
                        }
                        return html;
                    }
                },
                {
                    data: "voucherNo", bSortable: true,
                    render: function (data, id) {
                        if (data.voucherNo == null || data.voucherNo == undefined) {
                            return "";
                        } else {
                            return data.voucherNo;
                        }
                    }
                }
            ],
            checkChildCallback: function (data, elem) { //更新勾选信息
                //data为本行数据; $tr 代表本行jq对象
                //var c_rowdata=tabledata[elem.attr("row")];
                calCheckTotalData("cntrTb", data);
            },
            clickCallback: function (data, elem) {//更新勾选信息
                calCheckTotalData("cntrTb", data);
            },
            tableInitCallback: function (data) {
                calCheckTotalData("cntrTb", data);
                $("#cntrTb tr").each(function (index, eml) {//初始化表格背景色
                    if (index > 0) {
                        if (data[index - 1].compareSign == "1") {//已对账
                            eml.style.backgroundColor = "#4FBAE6";
                        } else if (data[index - 1].compareSign == "2") {//手工勾兑
                            eml.style.backgroundColor = "#B0DC81";
                        } else if (data[index - 1].compareSign == "4") {//本期未达
                            eml.style.backgroundColor = "#FF8080";
                        } else {//未对账
                            eml.style.backgroundColor = "#FFFF80";
                        }
                    }
                });
            },
            checkAllCallback: function (data) {//全选
                calCheckTotalData("cntrTb", data);
            }
        });


    }
    //加载银行日记账数据
    function loadBankCheckDatas(queryParam){
        BankCheckDatas = bt.bttable.init({
            elem:"#bankTb",
            checkbox:true,
            url:basePath+"/finacial/check/fccompareerp/toLoadCheckDatas",
            urldata:queryParam,
            contentType: "application/json",
            mulselect:true,
            headerCheckbox: true,
            selectCss:false,
            scrollX:true,
            ordering: true,
            formRenderInTable:true,
            scrollY:"370px",
            fixedColumns:{

            },
            columns:[
                {data:"tallyDate",className:'center',render:function(data,id){
                        return bt.date.format(data.tallyDate,"yyyy-mm-dd");
                    }
                },
                // {data:"billNo",render:function(data,id){
                //     if(data.billNo==null){
                //         return '';
                //     }else{
                //         return data.billNo;
                //     }
                // }},
                {data:"cdSign",className:'center',render:function(data,id){
                    if(data.cdSign=="1"){
                        return bt.lang("finacial.check.cdSign.debit");
                    }else{
                        return bt.lang("finacial.check.cdSign.credit");
                    }
                }},
                {data:"money",className:'textRight',
                    render:function(data,id){
                        return bt.money.format(data.money);
                    }
                },
                {data:"oppAccName",render:function(data,id){
                        var html = "";
                        if (data.oppAccName != null) {
                            html = '<div title="' + data.oppAccName + '" class="keep-all" style="max-width: 300px">' + data.oppAccName + '</div>';
                        }
                        return html;
                    }},
                {data:"voucherNo",
                    render:function(data,id){
                        if(data.voucherNo == null || data.voucherNo == undefined){
                            return "";
                        }else{
                            return data.voucherNo;
                        }
                    }
                }
            ],
            checkChildCallback:function(data,elem){//更新勾选信息
                calCheckTotalData("bankTb",data);
            },
            clickCallback:function(data){//更新勾选信息
                calCheckTotalData("bankTb",data);
            },
            tableInitCallback:function(data){//初始化表格背景色
                calCheckTotalData("bankTb",data);
                $("#bankTb tr").each(function(index,eml){
                    if(index>0){
                        if(data[index-1].compareSign=="1"){//已对账
                            eml.style.backgroundColor="#4FBAE6";
                        }else if(data[index-1].compareSign=="2"){//手工勾兑
                            eml.style.backgroundColor="#B0DC81";
                        }else if(data[index-1].compareSign=="4"){//本期未达
                            eml.style.backgroundColor="#FF8080";
                        }else{//未对账
                            eml.style.backgroundColor="#FFFF80";
                        }
                    }
                });
            },
            checkAllCallback:function(data,elem){//全选
                calCheckTotalData("bankTb",data);
            }
        })
    }

    //查询账号余额
    $getAccBalance = function(account, bankAcc, curId){
        $clearDatas();//清空数据
        var transDate = dateTypes.get("transTime").val();
        if(!transDate||transDate.trim()==""){

            layer.alert(bt.lang("finacial.check.selectEndDate"), {icon: 5});//选择结束日期
            return;
        }
        var paramType = $('#paramType').val();
        var accDisplayType = $("#accDisplayType").val();
        $.ajax({
            url:basePath+"/finacial/check/fccompareerp?getAccBalance",
            method:'POST',
            data:{
                "account":account,
                "bankAcc":bankAcc,
                "curId":curId,
                "transDate":transDate,
                "paramType":paramType,
                "accDisplayType":accDisplayType
            },
            dataType:'json',
            success:function(data){
                if(data.success&&data.obj){
                    $('#blnBalance').val(data.obj.blnBalance);
                    $('#bankBalance').val(data.obj.bankBalance);
                    $('#blnBalanceShow').html("<strong>"+bt.money.format(data.obj.blnBalance)+"</strong>");
                    $('#bankBalanceShow').html("<strong>"+bt.money.format(data.obj.bankBalance)+"</strong>");
                    $('#beginDate').html(data.obj.beginDate);
                    $('#checkDate').val(data.obj.lastOverDate);
                    //dateTypes.get("transTime").attr("data-min",data.obj.beginDate);
                    dateTypes.get("transTime").render();
                }else{
                    layer.alert(data.msg, {icon:5});
                    return false;
                }
            }
        });
    }

    //计算余额调节表数据
    function calAdjustBal(){
        var queryParam = {};
        queryParam = {"account" : $("#account").val(), "checkDate" : $("#checkDate").val(),
            "bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
            "paramType" : $('#paramType').val(),"curId": $("#curId").val(),
            "blnBalance" : $("#blnBalance").val(), "bankBalance" : $("#bankBalance").val()};
        $.ajax({
            url:basePath+"/finacial/check/fccompareerp?toLoadAdjustBalance",
            method:'POST',
            data:queryParam,
            dataType:'json',
            success:function(data){
                //显示调节后余额
                $("#blnAdjustBalance").val(bt.money.toNumber(data.obj.blnAdjustBalance));
                $("#bankAdjustBalance").val(bt.money.toNumber(data.obj.bankAdjustBalance));
                $("#adjustCntrMoney").html("<strong>"+ bt.money.format(data.obj.blnAdjustBalance)+"</strong>");
                $("#adjustBankMoney").html("<strong>"+ bt.money.format(data.obj.bankAdjustBalance)+"</strong>");
            }
        });

    }

    //勾兑操作
    $doOverOp = function(type, compareFlag){
        //勾兑：1 0
        //反勾兑：2 2
        //未达:3
        //TODO(对账) 勾兑操作
        if(cntrCount == 0 && bankCount == 0){//同一栏位勾兑
            if("1" == type){//勾对
                layer.alert(bt.lang("finacial.check.selectNumOp"),{icon:5});//请至少选择两条记录进行勾对！
                return;
            } else if ("2" == type){
                layer.alert(bt.lang("finacial.check.selectNumReverseOp"),{icon:5});//请至少选择两条记录进行反勾对！
                return;
            }
        }
        if(!$checkSelectData("cntrTb", type, compareFlag) || !$checkSelectData("bankTb", type, compareFlag)){
            return;
        }

        var cntrDatas = getSelectData("cntrTb", type, compareFlag);//中心对账选择数据
        var bankDatas = getSelectData("bankTb", type, compareFlag);//银行对账选择数据

        if(cntrCount == 0 || bankCount == 0){//单边勾对
            var checkDatas = {};
            if(cntrCount == 0){
                checkDatas = bankDatas;
            } else {
                checkDatas = cntrDatas;
            }
            var debitMoney = checkDatas["debitMoney"];//借方
            var creditMoney = checkDatas["creditMoney"];//贷方

            var debitLen = debitMoney.length;
            var creditLen = creditMoney.length;

            if(debitLen == 0 || creditLen == 0){//单边同借(贷)单笔勾兑
                var moneyLen = debitLen + creditLen;
                var overMoney = [];
                if(debitLen == 0){
                    overMoney = creditMoney;
                } else {
                    overMoney = debitMoney;
                }

                var sumMoney = 0;
                for(var i = 0; i < moneyLen; i++) {
                    if (overMoney[i] == "") {
                        continue;
                    }
                    var singleMoney = bt.money.toNumber(overMoney[i]);
                    if (singleMoney == 0) {
                        continue;
                    }
                    sumMoney = bt.money.add(sumMoney, singleMoney);
                }
                var equalFlag = false;
                if(sumMoney == 0){
                    equalFlag = true;
                }
                if(equalFlag == false){
                    if("1" == type){//勾对
                        //单笔勾对金额[{0}]，没有匹配的相同金额，不能进行勾对，请重新选择勾对数据！
                        layer.alert(bt.lang("finacial.check.message.single.sameMoney.notOp",new Array(bt.money.format(debitMon))), {icon:5});
                        return;
                    } else if ("2" == type){
                        //单笔反勾对金额[{0}]，没有匹配的相同金额，不能进行反勾对，请重新选择反勾对数据！
                        layer.alert(bt.lang("finacial.check.message.single.sameMoney.notReverseOp",new Array(bt.money.format(debitMon))),{icon:5});
                        return;
                    }
                }
            } else {//单边借贷单笔勾兑
                // if(debitLen != creditLen){
                //     if("1" == type){//勾对
                //         //单边勾对笔数不相等，不能进行勾对，请重新选择勾对数据！
                //         layer.alert(bt.lang("finacial.check.message.single.countNotEq.notOp"), {icon:5});
                //         return;
                //     } else if ("2" == type){
                //         //单边反勾对笔数不相等，不能进行反勾对，请重新选择反勾对数据！
                //         layer.alert(bt.lang("finacial.check.message.single.countNotEq.notReverseOp"), {icon:5});
                //         return;
                //     }
                // }
                // for(var i = 0; i < debitLen; i++){
                //     var debitMon = bt.money.toNumber(debitMoney[i]);
                //     var equalFlag = false;
                //     for(var j = 0; j < debitLen; j++){
                //         var creditMon = bt.money.toNumber(creditMoney[j]);
                //         if(debitMon == creditMon){
                //             equalFlag = true;
                //             creditMoney[j] = "";
                //             break;
                //         }
                //     }
                //     if(equalFlag == false){
                //         if("1" == type){//勾对
                //             //单笔勾对金额[{0}]，没有匹配的相同金额，不能进行勾对，请重新选择勾对数据！
                //             layer.alert(bt.lang("finacial.check.message.single.sameMoney.notOp",new Array(bt.money.format(debitMon))), {icon:5});
                //             return;
                //         } else if ("2" == type){
                //             //单笔反勾对金额[{0}]，没有匹配的相同金额，不能进行反勾对，请重新选择反勾对数据！
                //             layer.alert(bt.lang("finacial.check.message.single.sameMoney.notReverseOp",new Array(bt.money.format(debitMon))),{icon:5});
                //             return;
                //         }
                //     }
                // }
            }
        }else{//左栏和右栏勾兑
            var cntrDMoney = cntrDatas["debitMoney"];
            var cntrCMoney = cntrDatas["creditMoney"];
            var bankDMoney = bankDatas["debitMoney"];
            var bankCMoney = bankDatas["creditMoney"];
            var cntrDMoneyLen = cntrDMoney.length;
            var cntrCMoneyLen = cntrCMoney.length;
            var bankDMoneyLen = bankDMoney.length;
            var bankCMoneyLen = bankCMoney.length;
            var cntrDebitMoneySum = bt.money.toNumber(cntrDatas["debitMoneySum"]);
            var cntrCreditMoneySum = bt.money.toNumber(cntrDatas["creditMoneySum"]);
            var bankDebitMoneySum = bt.money.toNumber(bankDatas["debitMoneySum"]);
            var bankCreditMoneySum = bt.money.toNumber(bankDatas["creditMoneySum"]);

            if((cntrDMoneyLen == 0 && bankDMoneyLen == 0 && cntrCMoneyLen > 0 && bankCMoneyLen >0)//左借 = -右借/左贷 = -右贷(方向相同,正负相反)
                || (cntrDMoneyLen > 0 && bankDMoneyLen > 0 && cntrCMoneyLen == 0 && bankCMoneyLen == 0)){
                if(bt.money.add(cntrDebitMoneySum, bankDebitMoneySum) != 0 || bt.money.add(cntrCreditMoneySum, bankCreditMoneySum) != 0){
                    if("1" == type){//勾对
                        //两边勾对金额和不为零，不能进行勾对，请重新选择勾对数据！
                        layer.alert(bt.lang("finacial.check.message.much.sumNotZero.notOp"), {icon:5});
                        return;
                    } else if ("2" == type){
                        //两边反勾对金额和不为零，不能进行反勾对，请重新选择反勾对数据！
                        layer.alert(bt.lang("finacial.check.message.much.sumNotZero.notReverseOp"), {icon:5});
                        return;
                    }
                }
            }else if((cntrDMoneyLen==0&&cntrCMoneyLen>0&&bankDMoneyLen>0&&bankCMoneyLen>0)
                || (cntrCMoneyLen==0&&cntrDMoneyLen>0&&bankDMoneyLen>0&&bankCMoneyLen>0)){
                if((cntrDMoneyLen>0&&bt.money.sub(cntrDebitMoneySum, bt.money.sub(bankCreditMoneySum,bankDebitMoneySum))!=0)
                    ||(cntrCMoneyLen>0&&bt.money.sub(cntrCreditMoneySum,bt.money.sub(bankDebitMoneySum,bankCreditMoneySum))!=0)){
                    if("1" == type){
                        //两边勾对不满足对账公式(左借＝右贷－右借、左贷＝右借－右贷)
                        layer.alert(bt.lang("finacial.check.message.much.notMathMore.notOp"), {icon:5});
                        return;
                    }else if("2"==type){
                        //两边反勾对不满足对账公式(左借＝右贷－右借、左贷＝右借－右贷)
                        layer.alert(bt.lang("finacial.check.message.much.notMathMore.notReverseOp"), {icon:5});
                        return;
                    }
                }
            }else if(cntrDMoneyLen>0&&cntrCMoneyLen>0){
                if(bt.money.sub(bt.money.sub(cntrDebitMoneySum,cntrCreditMoneySum),bt.money.sub(bankCreditMoneySum,bankDebitMoneySum))!=0){
                    if("1"==type){
                        //两边勾对不满足对账公式(左借－左贷＝右贷－右借)
                        layer.alert(bt.lang("finacial.check.message.much.notMath.notOp"), {icon:5});
                        return;
                    }else if("2"==type){
                        //两边反勾对不满足对账公式(左借－左贷＝右贷－右借)
                        layer.alert(bt.lang("finacial.check.message.much.notMath.notReverseOp"), {icon:5});
                        return;
                    }
                }
            } else {//左借 = 右贷/左贷 = 右借(方向相反,正负相同)
                if(bt.money.sub(cntrDebitMoneySum, bankCreditMoneySum) != 0 || bt.money.sub(cntrCreditMoneySum, bankDebitMoneySum) != 0){
                    if("1" == type){//勾对
                        //两边勾对金额不相等，不能进行勾对，请重新选择勾对数据！
                        layer.alert(bt.lang("finacial.check.message.much.moneyNotEq.notOp"), {icon:5});
                        return;
                    } else if ("2" == type){
                        //两边反勾对金额不相等，不能进行反勾对，请重新选择反勾对数据！
                        layer.alert(bt.lang("finacial.check.message.much.moneyNotEq.notReverseOp"), {icon:5});
                        return;
                    }
                }
            }
        }

        var queryParam = {};
        queryParam = {"account" : $("#account").val(), "checkDate" : $("#checkDate").val(),
            "bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
            "paramType" : $('#paramType').val(),"opType":type,
            "ctrPk" : cntrDatas["dataPk"], "bankPk" : bankDatas["dataPk"]};
        $.ajax({
            url:basePath+"/finacial/check/fccompareerp?doHandOver",
            method:'POST',
            data:queryParam,
            dataType:'json',
            success:function(data){
                if(data.success){
                    if("1" == type){//勾对
                        layer.alert(bt.lang("finacial.check.op.succeed"), {icon:1});//勾兑成功
                    } else if("2" == type){
                        layer.alert(bt.lang("finacial.check.ReverseOp.succeed"), {icon:1});//反勾兑成功
                    }
                    $reloadTotalInfo();
                }
            }
        });
    }

    //确认未达操作
    function doHandStay(){
        if(cntrCount == 0 && bankCount == 0){
            layer.alert(bt.lang("finacial.check.selectNumNotArrive"),{icon:5});//请至少选择一条记录进行未达！
            return;
        }
        var type = "3";
        var compareFlag = [];
        compareFlag.push("0");
        compareFlag.push("4");
        if(!$checkSelectData("cntrTb", type, compareFlag) || !$checkSelectData("bankTb", type, compareFlag)){
            return;
        }

        var compareSign_cntr = $getCompareSign("cntrTb");
        var compareSign_bank = $getCompareSign("bankTb");
        if(compareSign_cntr != "" && compareSign_bank != "" && compareSign_cntr != compareSign_bank){
            layer.alert(bt.lang("finacial.check.notArriveNotSelectNotArrive"),{icon:5});//未达不能同时选择未勾对记录进行确认未达或者选择未达记录进行还原！
            return;
        }
        var cntrDatas = getSelectData("cntrTb", type, compareSign_cntr);//中心对账选择数据
        var bankDatas = getSelectData("bankTb", type, compareSign_bank);//银行对账选择数据
        if(compareSign_cntr == "4" || compareSign_bank == "4"){//已经确认未达
            type = "4";
        }

        var queryParam = {};
        queryParam = {"account" : $("#account").val(), "checkDate" : $("#checkDate").val(),
            "bankAcc" : $("#bankAcc")[0].value, "transDate" : dateTypes.get("transTime").val(),
            "paramType" : $('#paramType').val(),"opType":type,
            "ctrPk" : cntrDatas["dataPk"], "bankPk" : bankDatas["dataPk"]};
        $.ajax({
            url:basePath+"/finacial/check/fccompareerp?doHandOver",
            method:'POST',
            data:queryParam,
            dataType:'json',
            success:function(data){
                if(data.success){
                    //确认未达成功!
                    layer.alert(bt.lang("finacial.check.notArrive.succeed"), {icon:1});
                    $reloadTotalInfo();
                }else{
                    //确认未达失败!
                    layer.alert(bt.lang("finacial.check.notArrive.faild"), {icon:5});
                }
            }
        });

    }

    //获取对账类型
    $getCompareSign = function(tbId) {
        if(tbId=="cntrTb" && cntrTbData.length>0) {
            var compareSign = cntrTbData[0].compareSign;
            if(compareSign == null || compareSign == "" || compareSign == undefined || compareSign == "6"){
                compareSign = "0";
            }
            return compareSign;
        }
        if(tbId=="bankTb" && bankTbData.length>0){
            var compareSign = bankTbData[0].compareSign;
            if(compareSign == null || compareSign == "" || compareSign == undefined || compareSign == "6"){
                compareSign = "0";
            }
            return compareSign;
        }
        return '';
    }

    //获取选中数据
    function getSelectData(tbId, type, compareFlag){
        var datas;
        if(tbId=="cntrTb"){datas = cntrTbData;}
        if(tbId=="bankTb"){datas = bankTbData;}
        var length = datas.length;
        var flag = false;
        var moneys = {};
        var debitMoney = [];
        var creditMoney = [];
        var debitMoneySum = 0;
        var creditMoneySum = 0;
        var dataPk = "";
        if("1" == type || "2" == type || "3" == type){//勾对
            for(var i = 0; i < length; i++){
                var compareSign = datas[i].compareSign;
                if(compareSign == null || compareSign == "" || compareSign == undefined || compareSign == "6"){
                    compareSign = 0;//初始未达、为空也为未对账
                }
                var cdSign = datas[i].cdSign;
                var money = bt.money.toNumber(datas[i].money);
                // 增加自动勾兑数据也可进行反勾兑操作判断
                if(compareFlag == compareSign || ("2" == compareFlag && "1" == compareSign)){
                    if(cdSign == "1"){
                        debitMoney.push(money);
                        debitMoneySum = bt.money.add(debitMoneySum, money);
                    } else {
                        creditMoney.push(money);
                        creditMoneySum = bt.money.add(creditMoneySum, money);
                    }
                    dataPk += datas[i].id+",";
                }
            }
        }
        moneys["debitMoney"] = debitMoney;
        moneys["creditMoney"] = creditMoney;
        moneys["debitMoneySum"] = debitMoneySum;
        moneys["creditMoneySum"] = creditMoneySum;
        moneys["dataPk"] = dataPk;
        return moneys;
    }

    //校验选中数据
    $checkSelectData = function(tbId, type, compareFlag){
        var datas;
        if(tbId=="cntrTb"){datas = cntrTbData;}
        if(tbId=="bankTb"){datas = bankTbData;}

        var length = datas.length;
        var flag = false;
        var compareSignNo = false;
        var compareSignStay = false;
        for(var i = 0; i < length; i++){
            var compareSign = datas[i].compareSign;
            if(compareSign == null || compareSign == "" || compareSign == undefined || compareSign == "6"){
                compareSign = 0;//未达初始化、为空也为未对账
            }
            if("3" == type){//未达
                if(compareFlag[0] != compareSign && compareFlag[1] != compareSign){
                    flag = true;
                    break;
                }
                if(compareFlag[0] == compareSign){//确认未达
                    compareSignNo = true;
                }
                if(compareFlag[1] == compareSign){//还原
                    compareSignStay = true;
                }
            } else {
                if ("2" != type) {//自动勾兑数据也可以进行反勾兑，所以反勾兑不校验控制
                    if(compareFlag != compareSign){
                        flag = true;
                        break;
                    }
                }
            }
        }
        if(flag){
            if("1" == type){//勾对
                //勾对只能选择未勾对记录进行勾对！
                layer.alert(bt.lang("finacial.check.message.opNotOp"), {icon:5});
                return false;
            } else if ("2" == type){
                //反勾对只能选择已勾对记录进行反勾对！
                layer.alert(bt.lang("finacial.check.message.reverseOpSelectOp"), {icon:5});
                return false;
            } else if ("3" == type){
                //未达只能选择未勾对记录进行确认未达或者选择未达记录进行还原！
                layer.alert(bt.lang("finacial.check.message.notArriveSelectOpOrNotArrive"), {icon:5});
                return false;
            }
        } else {
            if ("3" == type && compareSignNo && compareSignStay){
                //未达不能同时选择未勾对记录进行确认未达或者选择未达记录进行还原！
                layer.alert(bt.lang("finacial.check.message.notArriveNotSelectOp"), {icon:5});
                return false;
            }
        }
        return true;
    }

    //结转操作
    function doAdjustOver(datas){
        if(overCheck(bt.lang("finacial.check.Op.Adjust"))){//结转
            return;
        }
        bt.loading.show();
        $.ajax({
            url:basePath+"/finacial/check/fccompareerp?doAdjustOverOther",
            method:'POST',
            data:datas.field,
            dataType:'json',
            success:function(data){
                if(data.success) {
                    //"结转成功!
                    layer.alert(bt.lang("finacial.check.Adjust.succeed"), {icon: 1});
                    refreshParentTab();
                    $("#adjustId").val(data.obj);
                    $clearDatas();
                    bt.loading.hide();
                }else{
                    //结转失败!
                    bt.loading.hide();
                    layer.alert(bt.lang("finacial.check.Adjust.faild"), {icon:5});
                }
            }
        });
    }

    //打印
    $doPrint = function(datas){
        if(bt.empty($("#adjustId").val())){
            layer.alert(bt.lang("finacial.check.adjusted.print"),{icon:7});
            return false;
        }else{
            parent.layer.open({
                type: 2,
                area: ['75%', '80%'],
                maxmin: true,
                content: basePath + "/finacial/check/fcbankbalanceadjust/viewCntrBankDtls/" + $("#adjustId").val() + "?param=1"
            });
        }
    }

    //结转判断
    function overCheck(title){
        if($("#adjustId").val() != ""){
            return false;
        }
        var checkFlag = $("#checkFlag").val();
        if(checkFlag == "0"){
            layer.alert(bt.lang("finacial.check.message.notCheck"), {icon:5});//未对账,请先对账！
            return true;
        }
        var blnAdjustBalance = $("#blnAdjustBalance").val();
        var bankAdjustBalance = $("#bankAdjustBalance").val();
        if(blnAdjustBalance && bankAdjustBalance){
            blnBalance = bt.money.toNumber(blnAdjustBalance);
            bankBalance = bt.money.toNumber(bankAdjustBalance);
            if(bt.money.add(blnBalance, 0 - bankBalance) - 0 != 0){
                //if(title.indexOf("导出")>=0)return false;
                //调节后余额不平，不能+title
                layer.alert(bt.lang("finacial.check.Op.AdjustBalanceNotEq",new Array(title)), {icon:5});
                return true;
            }
        }
        return false;
    }

    //清空数据
    $clearDatas = function(){
        $("#blnBalance").val('');
        $("#bankBalance").val('');
        $('#blnBalanceShow').html("");
        $('#bankBalanceShow').html("");
        $("#blnAdjustBalance").val('');
        $("#bankAdjustBalance").val('');
        $("#adjustCntrMoney").html("");
        $("#adjustBankMoney").html("");
        $("#checkFlag").val("0");
        //清空列表数据
        if(CntrCheckDatas != null && CntrCheckDatas != undefined){
            CntrCheckDatas.config.urldata = {};
            CntrCheckDatas.reload();
            BankCheckDatas.config.urldata = {};
            BankCheckDatas.reload();
        }
    }

    //初始化表格信息
    function initTbData(tbId){
        if("cntrTb"==tbId){
            cntrCount = 0;
            cntrDebMoney = 0;
            cntrCreMoney = 0;
        }else{
            bankCount = 0;
            bankDebMoney = 0;
            bankCreMoney = 0;
        }
    }



    //更新选中汇总数据
    function calCheckTotalData(tdId,data){
        var selectdata = new Array();
        if (data.length > 0) {
            bt.each(data,function(i,v){
                if(v.select){
                    selectdata.push(v);
                }
            });
        }
        initTbData(tdId);
        calCheckData(tdId,selectdata);
        if(tdId=="cntrTb"){cntrTbData = selectdata;}
        if(tdId=="bankTb"){bankTbData = selectdata;}

    }

    //设置勾选信息
    function calCheckData (tbId, rowData){
        if("cntrTb"==tbId){
            for(var i=0;i<rowData.length;i++){
                if(rowData[i].cdSign == "1"){
                    cntrDebMoney = bt.money.add(cntrDebMoney, rowData[i].money);
                }else{
                    cntrCreMoney = bt.money.add(cntrCreMoney, rowData[i].money);
                }
            }
            cntrCount = rowData.length;
            $("#cntrCount").html(rowData.length);
            $("#cntrDebMoney").html(bt.money.format(cntrDebMoney, {scale: 2}));
            $("#cntrCreMoney").html(bt.money.format(cntrCreMoney, {scale: 2}));
        }else{
            for(var i=0;i<rowData.length;i++){
                if(rowData[i].cdSign == "1"){
                    bankDebMoney = bt.money.add(bankDebMoney, rowData[i].money);
                }else{
                    bankCreMoney = bt.money.add(bankCreMoney, rowData[i].money);
                }
            }
            bankCount = rowData.length;
            $("#bankCount").html(rowData.length);
            $("#bankDebMoney").html(bt.money.format(bankDebMoney, {scale: 2}));
            $("#bankCreMoney").html(bt.money.format(bankCreMoney, {scale: 2}));
        }
        initDifferenceMoney();
    }

    //校验页面信息
    $doValidate = function(){
        var transDate = dateTypes.get("transTime").val();
        if(!transDate||transDate.trim()==""){
            //请先选择结束日期！
            layer.alert(bt.lang("finacial.check.selectEndDate"), {icon: 2});
            return false;
        }
        if($("#bankAcc")[0].value==""){
            //请选择银行账号
            layer.alert(bt.lang("finacial.check.selectBankAcc"), {icon:2});
            return false;
        }
        if($("#blnBalance").val()==""||$("#bankBalance").val()==""){
            //请先初始化账户余额
            layer.alert(bt.lang("finacial.check.firstInitBal"), {icon:2});
            return false;
        }
        return true;
    }

    //刷新父窗口
    function refreshParentTab(){
        if(bt.navtab){
            bt.navtab.refreshParentTable();
        }
    }

    //关闭窗口
    doClose = function(){
        if(bt.navtab){
            bt.navtab.closeCurrent();
        }
    }

    initDifferenceMoney=function(){
        cntrDifferenceMoney=bt.money.sub(cntrDebMoney,cntrCreMoney);
        // if(cntrDifferenceMoney<0){
        //     cntrDifferenceMoney=bt.money.mul(cntrDifferenceMoney,-1);
        // }
        bankDifferenceMoney=bt.money.sub(bankDebMoney,bankCreMoney);
        // if(bankDifferenceMoney<0){
        //     bankDifferenceMoney=bt.money.mul(bankDifferenceMoney,-1);
        // }
        differenceMoney=bt.money.sub(bt.money.sub(cntrDebMoney,bankCreMoney),bt.money.sub(cntrCreMoney,bankDebMoney));
        // if(differenceMoney<0){
        //     differenceMoney=bt.money.mul(differenceMoney,-1);
        // }
        $("#differenceMoney").html(bt.money.format(differenceMoney, {scale: 2}));
        $("#cntrDifferenceMoney").html(bt.money.format(cntrDifferenceMoney, {scale: 2}));
        $("#bankDifferenceMoney").html(bt.money.format(bankDifferenceMoney, {scale: 2}));
    }

    dateChangeCallback();

});
function resetSome() {
    moneyType.get("companyStartMoney").val("");
    moneyType.get("companyEndMoney").val("");
    moneyType.get("bankStartMoney").val("");
    moneyType.get("bankEndMoney").val("");
    dicts.get("cntrLangCode").val("");
    dicts.get("bankLangCode").val("");
    $("#cntrSummaryContent").val("");
    $("#oppAccName").val("");
    dateTypes.get("cntrStartTallyDate").render();
    dateTypes.get("cntrEndTallyDate").render();
    dateTypes.get("bankStartTallyDate").render();
    dateTypes.get("bankEndTallyDate").render();
    return false;
}
