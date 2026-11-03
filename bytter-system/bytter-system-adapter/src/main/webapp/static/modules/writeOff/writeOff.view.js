var moneyType;
bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {
     var $ = bt.jquery, form = bt.form();
     moneyType = bt.moneyType.init();

     var appTableSure = bt.datatable.initMultiTables({
         elem: "#appTableSure",
         //checkbox: false,
         //mulselect: true,
         selectCss: true,
         //jumpPageSelect: true,//开启跨页选择
         isInitData: true,
         searchForm: {
            formElem: "#sureSearchForm"
         },
         order: [[1, 'desc']],
         ajax: {
             'url': basePath + "/accountReceivable/writeOff/sureDatatable"
         },
         buttons: [
            {
                 extend: "colvis",
                 text: bt.lang('common.button.theadHide'),
                 columns: ':not(:first):not(:last)'
             }
         ],
         columns: [
            {data: "docManageVo.code", bSortable: false},
             {
                  data: "docMoney", bSortable: true, className: 'textRight',
                  render: function (data, type, full, meta) {
                      return bt.money.format(full.docMoney);
                  }
              },
             {data: "docManageVo.externalName", bSortable: true},
             {data: "docManageVo.corpName", bSortable: false},
             {data: "docManageVo.batchBusTypeName", bSortable: false},
             {data: "docManageVo.sourceName", bSortable: false},
             {data: "docManageVo.writeOffStatusName", bSortable: false},
             {data: "afterDocWriteOffStatusName", bSortable: false},
             {data: "writeOffTypeName", bSortable: false},
             {data: "docManageVo.receiptDate", bSortable: false},
             {data: "docManageVo.endDate", bSortable: false},
             {data: "docManageVo.agingDate", bSortable: false},
             {
               data: "docManageVo.writeOffBal", bSortable: true, className: 'textRight',
               render: function (data, type, full, meta) {
                   return bt.money.format(full.docManageVo.writeOffBal);
               }
             },
             {
                data: "afterDocWriteOffBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.afterDocWriteOffBal);
                }
             }
         ],
         btClickRowCallback: function (data1) {

         },
         btDrawCallback: function (setting) { //回调方法
         },
         summary: {
             pageSum: false,
             allSum: true,
             selectSum: false,
             format: {
                 decimal: 2
             },
             sumList: [
                 {
                     money: true,
                     colNum: '1',
                     dataName: 'docMoney'
                 }/*,{
                     money: true,
                     colNum:'13',
                     dataName:'docManageVo.writeOffBal'
                 },
                 {
                     money: true,
                     colNum:'14',
                     dataName:'afterDocWriteOffBal'
                 }*/
             ]
         }
     });


        //获取流水表格数据
        var tbDtl;
        function getDtlDate() {
            if (null == tbDtl) {
                tbDtl = bt.datatable.initMultiTables({
                    elem: "#appTableDtl",
                    isInitData: true,
                    checkbox: false,
                    mulselect: true,
                    selectCss: true,
                    jumpPageSelect: false,//开启跨页选择
                    searchForm: {
                        formElem: "#searchFormDtl",
                        formFilter: "dtlSearch"
                    },
                    order: [[1, 'desc']],
                    ajax: {
                        'url': basePath + "/accountReceivable/writeOff/dtlDatatable"
                    },
                    buttons: [
                        {
                            extend: "colvis",
                            text: bt.lang('common.button.theadHide'),
                            columns: ':not(:first):not(:last)'
                        }
                    ],
                    columns: [
                        {data: "bankSerialNo", bSortable: true},
                        {data: "transTime", bSortable: true},
                        {data: "curName", bSortable: true},
                        {
                            data: "amt", bSortable: true, className: 'textRight',
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.amt);
                            }
                        },
                        {
                            data: "writeOffBal", bSortable: true, className: 'textRight',
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.writeOffBal);
                            }
                        },
                        {data: "accName", bSortable: true},
                        {data: "bankName", bSortable: true},
                        {data: "bankAcc", bSortable: true},
                        {data: "oppAccName", bSortable: true},
                        {data: "oppAccNo", bSortable: true},
                        {data: "returnTime", bSortable: true, format: "YYYY-MM-DD hh:mm:ss"},
                        {data: "uses", bSortable: true},
                        {data: "postscript", bSortable: true},
                        {data: "writeOffStatusName", bSortable: true},
                        {data: "writeOffTypeName", bSortable: true}
                    ],
                    btDrawCallback: function () {
                    },
                    btClickRowCallback: function (data1) {
                    }
                });
            } else {
                bt.datatable.refreshTable("appTableDtl");
            }
        }

        var tbBill;
        function getBillData() {
            if (null == tbBill) {
                tbBill = bt.datatable.initMultiTables({
                    elem: "#appTableBill",
                    checkbox: false,
                    mulselect: true,
                    selectCss: true,
                    isInitData: true,
                    jumpPageSelect: false,//开启跨页选择
                    searchForm: {
                        formElem: "#searchFormBill",
                        formFilter: "billSearch"
                    },
                    order: [[1, 'desc']],
                    ajax: {
                        'url': basePath + "/accountReceivable/writeOff/billDatatable"
                    },
                    buttons: [
                        {
                            extend: "colvis",
                            text: bt.lang('common.button.theadHide'),
                            columns: ':not(:first):not(:last)'
                        }
                    ],
                    columns: [
                        {data: "billTypeName", bSortable: true},
                        {data: "billFlagName", bSortable: true},
                        {data: "corpCodeAndName", bSortable: true},
                        {data: "blRmtrUnnIdNo", bSortable: true},
                        {data: "payer", bSortable: true},
                        {data: "acceptor", bSortable: true},
                        {data: "origNo", bSortable: true},
                        {data: "billMoney", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.billMoney);
                            }
                        },
                        {data: "realMoney", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.realMoney);
                            }
                        },
                        {data: "writeOffBal", bSortable: true,
                             render: function (data, type, full, meta) {
                                 return bt.money.format(full.writeOffBal);
                             }
                         },
                        {data: "startDate", bSortable: true},
                        {data: "endDate", bSortable: true},
                        {data: "receDate", bSortable: true},
                        {data: "briefIdName", bSortable: true},
                        {data: "enterStockName", bSortable: true},
                        {data: "newStmIssuofpprIndName", bSortable: true},
                        {data: "blgrdbagcrclPrmtIndName", bSortable: true},
                        {data: "sbbllRngStrtSn", bSortable: true},
                        {data: "sbbllRngEndSn", bSortable: true},
                        {data: "blStdAmt", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.blStdAmt);
                            }
                        },
                        {data: "transferWayName", bSortable: true,
                            render: function (data, type, full, meta) {
                                if (full.briefId == '8018' && full.transferWay == '0') {
                                    data = '票据池质押';
                                }
                                return data;
                            }
                        },
                        {data: "writeOffStatusName", bSortable: true},
                        {data: "writeOffTypeName", bSortable: true}
                    ],
                    btDrawCallback: function () {
                    },

                });
            } else {
                bt.datatable.refreshTable("appTableBill");
            }
        }

        var tbLc;
        function getLcData() {
            if (null == tbLc) {
                tbLc = bt.datatable.initMultiTables({
                    elem: "#appTableLc",
                    isInitData: true,
                    checkbox: false,
                    mulselect: true,
                    selectCss: true,
                    jumpPageSelect: false,//开启跨页选择
                    searchForm: {
                        formElem: "#searchFormLc",
                        formFilter: "lcSearch"
                    },
                    order: [[1, 'desc']],
                    ajax: {
                        'url': basePath + "/accountReceivable/writeOff/lcDatatable"
                    },
                    buttons: [
                        {
                            extend: "colvis",
                            text: bt.lang('common.button.theadHide'),
                            columns: ':not(:first):not(:last)'
                        }
                    ],
                    columns: [
                        {data: "arrivalCode", bSortable: true},
                        {data: "corpName", bSortable: true},
                        {data: "creditNo", bSortable: true},
                        {data: "lcInter", bSortable: true,
                            render: function (data, type, full, meta) {
                                return bt.dict("letter_of_credit_inter", full.lcInter);
                            }
                        },
                        {data: "handDocBank", bSortable: true},
                        //{data: "beneficiary", bSortable: true},
                        {data: "corpName", bSortable: true, className: "center"},
                        {
                            data: "billMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.billMoney);
                            }
                        },
                        {
                            data: "payMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.payMoney);
                            }
                        },
                        {
                            data: "incomingMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.incomingMoney);
                            }
                        },
                        {
                            data: "writeOffBal", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.writeOffBal);
                            }
                        },
                        {
                            data: "mortgageMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.mortgageMoney);
                            }
                        },
                        {
                            data: "negotiationMoney", bSortable: true, className: "textRight",
                            render: function (data, type, full, meta) {
                                return bt.money.format(full.negotiationMoney);
                            }
                        },
                        {data: "curName", bSortable: true},
                        {data: "paymentDate", bSortable: true, className: "center"},
                        {data: "corpName", bSortable: true},
                        {data: "writeOffStatusName", bSortable: true},
                        {data: "writeOffTypeName", bSortable: true}
                    ],
                    btDrawCallback: function () {
                    },

                });
            } else {
                bt.datatable.refreshTable("appTableLc");
            }
        }

        function initRelationTable(){
            var paymentMeansSel = $("#paymentMeans").val();
            if("1" == paymentMeansSel){
                getDtlDate()
            }else if("2" == paymentMeansSel){
                getBillData()
            }else if("3" == paymentMeansSel){
                getLcData()
            }
        }
        initRelationTable();


     form.on('submit(submit)', function (data) {
        bt.loading.show();
        var action = "/accountReceivable/writeOff/sureWriteOff";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide();
            parent.layer.open({content:result.msg, yes: function(){
                parent.layer?parent.layer.closeAll():layer.closeAll();
                bt.navtab.refreshCurrentTable("appTableDoc");
            }
            });
        },'JSON');
        return false;
     });
});

function cancle(){
    parent.layer?parent.layer.closeAll():layer.closeAll();
}