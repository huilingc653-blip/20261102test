// APP-H5桥接代码
if (!window.BTAppBridge) {
    window.BTAppBridge = {
        toggleScale: function (index) {
            var fontSizeElem = document.getElementsByTagName('*');
            for (var i = 0; i < fontSizeElem.length; i++) {
                var size = getComputedStyle(fontSizeElem[i]).fontSize.replace('px', '');
                fontSizeElem[i].style.fontSize = (index % 2 === 1 ? parseFloat(size) * 1.4 : parseFloat(size) / 1.4) + 'px';
            }
        },
        post: function (url, params, callback) {
            var obj = {
                type: 'requestData',
                method: 'post',
                params: params || {},
                callback: callback
            };
            if (obj.params) {
                obj.params.forwardURL = url;
            }
// alert(url)
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        get: function (url, params, callback) {
            var obj = {
                type: 'requestData',
                method: 'get',
                params: params || {},
                callback: callback
            };
            obj.params.forwardURL = url;
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        open: function (obj) {
            obj = obj || {};
            obj.type = 'open';
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        openTab: function (obj) {
            obj = obj || {};
            obj.type = 'openTab';
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        jump: function (obj) {
            obj = obj || {};
            obj.type = 'jump';
            window.parent.postMessage(JSON.stringify(obj), '*');
            // alert(JSON.stringify(obj))
            return obj
        },
        refresh: function (obj) {
            obj = obj || {};
            obj.type = 'refresh';
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        datePicker: function (obj) {
            obj = obj || {};
            obj.type = 'datePicker';
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        singleDatePicker: function (obj) {
            obj = obj || {};
            obj.type = 'singleDatePicker';
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        openDatePickerPage: function (callback, configs) {
            var obj = {};
            obj.type = 'openDatePickerPage';
            obj.callback = callback;
            if (configs) {
                obj.configs = configs;
            } else {
                obj.configs = null;
            }
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        openCorpSelectionPage: function (callback, configs) {
            var obj = {};
            obj.type = 'openCorpSelectionPage';
            obj.callback = callback;
            if (configs) {
                obj.configs = configs;
            } else {
                obj.configs = null;
            }
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        openChartModulesManagePage: function (data) {
            var obj = {};
            obj.type = 'openChartModulesManagePage';
            obj.data = data;
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        },
        openChartTabPage: function (data) {
            var obj = {};
            obj.type = 'openChartTabPage';
            obj.data = data;
            window.parent.postMessage(JSON.stringify(obj), '*');
            return obj
        }
    }
}
// window.document.addEventListener('message', function (event) {
//     var data = JSON.parse(event.data);
//     switch(data.type){
//         case 'toggleScale':
//             window.BTAppBridge.toggleScale(data.index);
//             window.scaleAction && window.scaleAction(data.index);
//             break;
//         case 'search':
//             window.searchAction && window.searchAction();
//             break;
//         case 'requestData':
//             if(data.callback){
//                 var Fn = Function;
//                 var func = new Fn('return ' + data.callback)();
//                 func && func(data.result);
//             }
//             break;
//         case 'datePicker':
//             var obj={};
//             if(data.date){
//                 obj.date = data.date;
//             }
//             if(obj.scope){
//                 obj.scope = data.scope;
//             }
//             window.datePickerAction && window.datePickerAction(obj);
//             break;
//     }
// },false);

if (!window.BTAppChartReportBridge) {
    // 针对app报表页面，用于统一处理app传过来的数据,执行相对应的方式
    window.BTAppChartReportBridge = function (rData) {
        if (!rData) {
            return
        }
        var url = rData.url;
        var type = rData.type;
        var data = rData.data;
        var callBack = rData.callback;
        // alert(type + '——' + callBack)
        switch (url) {
            case '/mobile/api/bankacc/mobileBankAcc':
                if (type === 'list') {
                    getResultList && getResultList(data)
                } else {
                    getCorp && getCorp(data)
                }
                break;
            case '/mobile/budget/fbsbudget/mobileReqInput':
                if (type === 'list') {
                    getMobileReqInput && getMobileReqInput(data)
                } else if (type === 'cord') {
                    getSearchMobileReq && getSearchMobileReq(data)
                } else {
                    getSearchMobileReq && getSearchMobileReq(data)
                }
                break;
            case '/mobile/chowmatistic/choproductreqquery/mobileReq':
                if (type === 'list') {
                    getFinancialList && getFinancialList(data)
                } else {
                    getCallback && getCallback(data)
                }
                break;
            case '/mobile/cms/provide/loaninfo/loanMobileQuery':
                if (type === 'list') {
                    getBalDataList && getBalDataList(data)
                } else if (type === 'searchActionCallback') {
                    // 发放日
                    searchActionCallback && searchActionCallback(data)
                } else if (type === 'searchEndActionCallback') {
                    // 到期日
                    searchEndActionCallback && searchEndActionCallback(data)
                } else {
                    getBalDataSeach && getBalDataSeach(data)
                }
                break;
            case '/mobile/cms/guarantee/guaContractInfo/appReportlist':
                if (type === 'list') {
                    getReportData && getReportData(data)
                } else if (type === 'dateActionCallback') {
                    getDateActionCallback && getDateActionCallback(data)
                } else {
                    getReportDataSeach && getReportDataSeach(data)
                }
                break;
            case '/mobile/report/fundsbalancestatistics/mobileBalance':
                if (type === 'list') {
                    setTimeout(() => {
                        getMobileBalance && getMobileBalance(data)
                    }, 1000)
                } else if (type === "refreshData") {
                    getMobileBalanceRefreshData && getMobileBalanceRefreshData(data)
                } else {
                    setTimeout(() => {
                        getMobileBalanceSeach && getMobileBalanceSeach(data)
                    }, 1100)
                }
                break;
            case '/mobile/report/cms/cmsbalancegather/gatherApp':
                if (type === 'list') {
                    getMobileBalance && getMobileBalance(data)
                } else {
                    setTimeout(() => {
                        getCreditSeach && getCreditSeach(data)
                    }, 1000)
                }
                break;
            case '/mobile/report/cms/loanTrend/mobileList':
                if (type === 'list') {
                    // 列表、图表
                    setTimeout(() => {
                        if (callBack === 'refreshchart') {
                            getMobileChart && getMobileChart(data)
                        }else {
                            getMobileList && getMobileList(data)
                        }
                    }, 1000)
                } else if (type === 'cord') {
                    // 搜索
                    setTimeout(() => {
                        getMobileSeach && getMobileSeach(data)
                    }, 1000)
                } else if (type === 'Bal') {
                    setTimeout(() => {
                        getMobile && getMobile(data)
                    }, 1000)
                } else {
                    setTimeout(() => {
                        getTimeSeach && getTimeSeach(data)
                    }, 1000)
                }
                break;
            case '/mobile/bill/accebill/balCompare/balCompareApp':
                if (type === 'list') {
                    getBalCompareApp && getBalCompareApp(data)
                } else {
                    setTimeout(() => {
                        getBalCompareApSeach && getBalCompareApSeach(data)
                    }, 1000)

                }
                break;
            case '/mobile/report/cms/cmsExpireAnalysis/mobileExpireAnalysi':
                if (type === 'list') {
                    setTimeout(() => {
                        if (callBack === 'refreshData') {
                            getMobileTable && getMobileTable(data)
                        }else {
                            getMobileData && getMobileData(data)
                        }
                    }, 1000)
                } else if (type === 'Bal') {
                    setTimeout(() => {
                        setTimeout(() => {
                            getMobileTable && getMobileTable(data)
                        }, 1000)
                    }, 1000)
                } else {
                    setTimeout(() => {
                        getSearchAction && getSearchAction(data)
                    }, 1000)
                }
                break;
            case '/mobile/report/cms/loanBalanceAnalysis/appList':
                if (type === 'list') {
                    // getLoanBalanceAnalysis && getLoanBalanceAnalysis(data)
                    setTimeout(() => {
                        if (callBack === 'postBalChartData') {
                            getLoanBalanceAppBal && getLoanBalanceAppBal(data)
                        }else {
                            getLoanBalanceAnalysis && getLoanBalanceAnalysis(data)
                        }
                    }, 100)
                } else if (type === 'Bal') {
                    setTimeout(() => {
                        getLoanBalanceAppBal && getLoanBalanceAppBal(data)
                    }, 1000)
                } else {
                    setTimeout(() => {
                        getSearchAction && getSearchAction(data)
                    }, 1000)
                }
                break;
            case '/mobile/budget/fbsbudget/mobileReqProess':
                if (type === 'list') {
                    getMobileReqProess && getMobileReqProess(data)
                } else if (type === 'cord') {
                    getSearchMobileReqProess && getSearchMobileReqProess(data)
                } else {
                    getSearchMobileReqProess && getSearchMobileReqProess(data)
                }
                break;
            case '/mobile/report/netbank/mobileAllocation':
                if (type === 'list') {
                    getMobileAllocation && getMobileAllocation(data)
                } else if (type === 'cord') {
                    getMobileAllocationDate && getMobileAllocationDate(data)
                } else {
                    setTimeout(() => {
                        getMobileAllocationDate && getMobileAllocationDate(data)
                    }, 1000)
                }
                break;
            case '/mobile/report/cms/ratbkstatistics/mobileBkAmountRation':
                if (type === 'list') {
                    getMobileBkAmountRation && getMobileBkAmountRation(data)
                } else if (type === 'cord') {
                    setTimeout(() => {
                        getSeachrMobileBkAmountRation && getSeachrMobileBkAmountRation(data)
                    }, 1000)
                } else if (type === 'datePickerCallback') {
                    setTimeout(() => {
                        getSeachrMobileBkCallback && getSeachrMobileBkCallback(data)
                    }, 1000)
                } else {
                    setTimeout(() => {
                        getSeachrMobileBkCallbackEnd && getSeachrMobileBkCallbackEnd(data)
                    }, 1000)
                }
                break;
            case '/mobile/bi/capitalStock/listNetMoblie':
                if (type === 'list') {
                    getListNetMoblie && getListNetMoblie(data)
                } else if (type === 'searchActionCallback') {
                    setTimeout(() => {
                        getListNetMoblieSearchActionCallback && getListNetMoblieSearchActionCallback(data)
                    }, 1000)
                }
                break;
            case '/mobile/bill/accebill/soonExpireBill/mobile/list':
                if (callBack === 'postBalData') {
                    setTimeout(() => {
                        getSoonExpireBillBal && getSoonExpireBillBal(data)
                    }, 1000)
                }else if (type === 'list') {
                    setTimeout(() => {
                        getSoonExpireBill && getSoonExpireBill(data)
                    }, 1000)
                } else if (type === 'searchActionCallback') {
                    setTimeout(() => {
                        getListNetMoblieSearchActionCallback && getListNetMoblieSearchActionCallback(data)
                    }, 1000)
                }else if(type === 'cord' ){
                    searchActionCallback && searchActionCallback(data)
                }
                break;
            default:
                if(callBack){
                    var back = makeCallBack(callBack);
                    setTimeout(() =>{
                        back && back(data)
                    },1000)
                }
                break;
        }
    }
}

function makeCallBack(callBack){
    if(callBack && typeof(callBack) != "function"){
        //校验下拉树的回调方法是否存在
        try {
            callBack = eval(callBack);
        } catch(e) {
            alert(callBack + 'not exist！');
        }
    }
    return callBack;
}
