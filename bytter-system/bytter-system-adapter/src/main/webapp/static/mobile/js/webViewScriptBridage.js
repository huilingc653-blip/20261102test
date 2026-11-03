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
            if(obj.params){
                obj.params.forwardURL = url;
            }

            window.parent.postMessage(JSON.stringify(obj),'*');
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
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        open: function (obj) {
            obj = obj || {};
            obj.type = 'open';
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        openTab: function (obj) {
            obj = obj || {};
            obj.type = 'openTab';
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        jump: function (obj) {
            obj = obj || {};
            obj.type = 'jump';
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        refresh: function (obj) {
            obj = obj || {};
            obj.type = 'refresh';
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        datePicker: function (obj) {
            obj = obj || {};
            obj.type = 'datePicker';
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        singleDatePicker: function (obj) {
            obj = obj || {};
            obj.type = 'singleDatePicker';
            window.parent.postMessage(JSON.stringify(obj),'*');
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
            window.parent.postMessage(JSON.stringify(obj),'*');
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
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        openChartModulesManagePage: function (data) {
            var obj = {};
            obj.type = 'openChartModulesManagePage';
            obj.data = data;
            window.parent.postMessage(JSON.stringify(obj),'*');
            return obj
        },
        openChartTabPage: function (data) {
            var obj = {};
            obj.type = 'openChartTabPage';
            obj.data = data;
            window.parent.postMessage(JSON.stringify(obj),'*');
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
