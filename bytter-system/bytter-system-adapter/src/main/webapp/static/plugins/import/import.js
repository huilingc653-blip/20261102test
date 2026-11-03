/**
 * Created by zhouj on 2017/9/4.
 * 0.0.1 封装组件
 * 0.0.2 解决分页栏nextPage样式不对的问题 liuyj
 */
var importext = layui.define(['layer', 'jquery', 'element', 'page', 'form', 'uploadExcel', 'laytpl', 'datatablecore', 'html5', 'datatablePrint', 'datatableButtons', 'moneyType'], function (exports) {
    form = layui.form();
    laytpl = layui.laytpl;
    jQuery = layui.jquery, element = layui.element(), laypage = layui.laypage;
    $ = layui.jquery;
    var moneyFun = bt.moneyType.getFun();
    var regexZero = /:+(\-?\d+\.+[0-9]*[0])+\,/g;
    var lang = {
        sProcessing: "<div class='loader'>" + bt.lang("common.table.loading") + "</div>",
        sLengthMenu: bt.lang("common.table.lengthMenu"),
        sZeroRecords: bt.lang("common.msg.temporarily_no_data"),
        sInfo: bt.lang("common.table.info"),
        sInfoEmpty: bt.lang("common.table.infoEmpty"),
        sInfoFiltered: bt.lang("common.table.infoFiltered"),
        sInfoPostFix: "",
        sSearch: bt.lang("common.table.local.search"),
        sUrl: "",
       // sEmptyTable: bt.lang("common.msg.temporarily_no_data"),
        sEmptyTable: '<img src="'+ basePath +'/static/v3.2/images/pic_blank.png" class="pic-bank"><div class="empty-text">'+bt.lang('common.msg.temporarily_no_data')+'</div>',//"暂无数据",
        sLoadingRecords: bt.lang("common.table.loading"),
        sInfoThousands: ",",
        oPaginate: {
            sFirst: bt.lang("common.table.firstPage"),
            sPrevious: bt.lang("common.table.previousPage"),
            sNext: bt.lang("common.table.nextPage"),
            sLast: bt.lang("common.table.lastPage"),
            sJump: bt.lang("common.table.jump")
        },
        oAria: {
            sSortAscending: bt.lang("common.table.sortAscending"),
            sSortDescending: bt.lang("common.table.sortDescending")
        }
    };
    //格式化国际化
    String.prototype.formatLang = function () {
        var msg = "";
        if (this) {
            var errors = this.split(",");
            if (errors) {
                for (var i = 0; i < errors.length; i++) {
                    if (errors[i]) {
                        msg += bt.lang(errors[i]) + ",";
                    }
                }
            }
        }
        return msg.replace(/,$/, "");
    };
    String.prototype.formatLangByCode = function (fName) {
        if (this) {
            if (LangMap && LangMap[fName]) {
                return bt.dict(LangMap[fName], this);
            }
        }
        return this;
    };
    String.prototype.formatThousandSplit = function (isSplit) {
        if (this && this.length > 0 && isSplit) {
            if (isSplit === "thousand") {
                return moneyFun.format(this.toString(), {separator: ",", decimal: 4});
            } else if (isSplit === "percent") {
                return moneyFun.format(this.toString());
            } else if (isSplit === "todigit") {
                return moneyFun.toChinese(this.toString());
            } else if (isSplit === "million") {
                return moneyFun.convertMoney(this.toString());
            } else if (isSplit === "yuan") {
                return moneyFun.convertMoney(this.toString(), "yuan");
            }
        }
        return this;
    };
    var parseParam = function (param, key) {
        if (!param) return "";
        var paramStr = "";
        if (param instanceof String || param instanceof Number || param instanceof Boolean) {
            paramStr += "&" + key + "=" + encodeURIComponent(param);
        } else {
            $.each(param, function (i) {
                var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
                paramStr += '&' + parseParam(this, k);
            });
        }
        return paramStr.substr(1);
    };

    var parseCustomParame = function () {
        var obj = $("#customParameForm").serializeCustomParame();
        return obj;
    }
    //组装form表单参数对象
    $.fn.serializeCustomParame = function () {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (o[this.name] !== undefined) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push($.trim(this.value) || '');//this value添加trim方法 liuyj 2018-05-23
            } else {
                o[this.name] = $.trim(this.value) || '';//this value添加trim方法 liuyj 2018-05-23
            }
        });
        return o;
    }

    function isArray(arr) {
        return Object.prototype.toString.call(arr) === '[object Array]';
    }

    var importTplId = "importTpl", errorViewImportId = "errorview", viewImportId = "view",
        allImportCheckName = "allcheck", itemImportCheckName = "itemcheck", tdImportCheckName = "itemtd";
    var saveData = [], AllheadMap = {}, headData = [], selectData = [], errorTotal = 0, callBackFun, LangMap = {},
        cacheKey = "", _pageNo = 0,importIndex,windownIndex,summary = 0, errorSummary = 0;//所有的数据
    var _cacheCode = ""; // 新增导入功能缓存，cacheKey作为分页缓存使用 zuoy 2019-06-13


    /*
    *  导入成功之后执行的方法
    */
    var checkFun = function (index,that) {
        windownIndex=index;
        // bt.loading.show();
        if (cacheKey) {
            that.options.success && that.options.success(that.getHeadRows(), cacheKey, saveData, _cacheCode);
            callBackFun && callBackFun(that.getHeadRows(), cacheKey, saveData, _cacheCode);
        } else {
            that.options.success && that.options.success(that.getHeadRows(), saveData, saveData, _cacheCode);
            callBackFun && callBackFun(that.getHeadRows(), saveData, saveData, _cacheCode);
        }
        if(that.options.autoCloseWindown) {
            bt.loading.hide();
            $('.import_excel_common #data_tab .dataTables_scrollBody table').DataTable().destroy(true);
            $('.import_excel_common #error_data_tab .dataTables_scrollBody table').DataTable().destroy(true);
            layer.close(index);
        }
    };

    var BTImport = function () {
        this.options = {
            code: undefined,//模板code
            url: basePath + '/import/excel/upload',//导入url
            downUrl: undefined,//模板下载url 兼容自定义模板下载
            customParameUrl: undefined,//自定义参数页面地址 zuoy 2019-05-15
            titleRows: 0,//表格标题行数下标,默认0
            headRows: 1,//表头行数下标,默认1
            endRows: 0,
            errorMsg: "errorMsg",//错误信息字段名称
            langMap: "langMap",//字段与字典关系名称
            width: '1000px',//弹出框宽度 '600px'
            height: '600px',//弹出框高度 '400px'
            autoCloseWindown: true,//默认自动关闭窗体
            page: false,//是否分页
            sumMoney: false, //是否汇总
            // pageLengthMenu:[10,25,50,100],//当表格每页显示多少条数据数组
            // pageLen:10,//默认表格每页显示多少条

            isShowData: true,//是否显示导入数据
            params: undefined,//参数json对象,
            success: undefined,
            hideDownTempBtn: false, //隐藏下载模板按钮 zuoy 2019-05-16
            hideExportBtn:true, // 隐藏下载按钮
        };
        return this;
    };
    BTImport.prototype.getRandom = function (num) {
        var Num = "";
        for (var i = 0; i < num; i++) {
            Num += Math.floor(Math.random() * 10);
        }
        return Num;
    };
    //初始化html
    BTImport.prototype.initHtml = function (config, callBack) {
        var that = this;
        /*if($("#"+viewImportId).length>0){
            viewImportId = "view"+that.getRandom(4);
        }*/
        var downUrl = config.downUrl ? config.downUrl : basePath + '/download/resource?code=' + config.code;
        var tabhtml = '<div class="layui-tab layui-tab-brief" ';
        if (!config.isShowData) {
            tabhtml +=' style="display:none" ';
        }
        tabhtml += ' lay-filter="docDemoTabBrief" style="margin:0;">\
                <ul class="layui-tab-title">\
                <li class="layui-this">' + bt.lang("common.import.qualified.data") + '</li>\
                <li>' + bt.lang("common.import.error.data") + '</li>\
                </ul>\
                <div class="layui-tab-content" style="padding: 0 10px;">\
                <div class="layui-tab-item layui-show import-tablecss" style="" id="data_tab">\
                <div class="layui-form" id="view" style="overflow:auto;min-height:300px;"></div><div id="viewPage"></div></div>\
                <div class="layui-tab-item import-tablecss" id="error_data_tab">\
                <div class="layui-form" id="errorview" style="overflow:auto;min-height:300px;"></div><div id="errorviewPage"></div></div>\
                </div>\
                </div>';
        var html = '<script id="' + importTplId + '" type="text/html">' +
            '{{# if(d && d.map)}}' +
            '<table class="layui-table">' +
            '<thead>' +
            '<tr>' +
            '<th>' + bt.lang("table.fcCompareAcc.bankId") + '</th>' +
            //'<th><input type="checkbox" name="'+allImportCheckName+'" lay-skin="primary" lay-filter="'+allImportCheckName+'" checked="checked"></th>'+
            '{{# for(var k in d.map){ }}' +
            '<th>{{ k }}</th>' +
            '{{# } }}' +

            // 判断是否有错误列，合格数据隐藏错误说明列
            '{{# var errorCol = false; }}' +
            '{{# if(d.list && d.list.length>0){  }}' +
            '{{# for(var i = 0, len = d.list.length; i < len; i++){ }}' +
            '{{# if(d.list[i]["' + config.errorMsg + '"]){  }}' +
            '{{# errorCol = true;break;   }}' +
            '{{# } }}' +
            '{{# } }}' +
            '{{# }  }}' +

            '{{# if(errorCol){  }}' +
            '<th >' + bt.lang("table,import.errorMsg") + '</th>' +
            '{{# }  }}' +
            '</tr>' +
            '</thead>' +
            '<tbody>' +
            '{{# if(d.list && d.list.length>0){  }}' +
            '{{# for(var i = 0, len = d.list.length; i < len; i++){ }}' +
            '{{# var bgcolor = ""  }}' +
            '{{# if(d.list[i]["' + config.errorMsg + '"]){  }}' +
            '{{# bgcolor = "style=\'background-color:#FF5722;\'"  }}' +
            '{{# }  }}' +
            '<tr {{ bgcolor }}>' +
            '<td style="padding-top: 0;padding-bottom: 0;">{{ (d.pageNo + i + 1) }}</td>' +
            // '<td>{{ (' + _pageNo + ' + i) }}</td>' +
            //'{{# if(!d.list[i]["'+config.errorMsg+'"]){  }}'+
            //'<td><input type="checkbox" name="'+itemImportCheckName+'" data-index="{{ i }}" lay-skin="primary" lay-filter="'+itemImportCheckName+'" checked="checked" ></td>'+
            //'{{# }else{  }}'+
            //'<td></td>'+
            //'{{# }  }}'+
            '{{# for(var k in d.map){ }}' +
            '<td class="' + tdImportCheckName + '" align="{{d.alignment?d.alignment[d.map[k]]?d.alignment[d.map[k]]:"":""}}" style="padding-top: 0;padding-bottom: 0;">' +
            '' + bt.lang("{{ (d.list[i][d.map[k]]!=null?d.list[i][d.map[k]]+'':'').formatLangByCode(d.map[k]).formatThousandSplit(d.split?d.split[d.map[k]]?d.split[d.map[k]]:'':'') }}") + '</td>' +
            '{{# } }}' +
            '{{# if(d.list[i]["' + config.errorMsg + '"]){  }}' +
            '<td>{{ d.list[i]["' + config.errorMsg + '"]?d.list[i]["' + config.errorMsg + '"].formatLang():"" }}</td>' +
            '{{# } }}' +
            '</tr>' +
            '{{# } }}' +
            '{{# } }}' +
            '</tbody>' +
            '</table>' +
            '{{#}}' +
            '</script><div class="import_excel_common">' +
            '<blockquote class="layui-elem-quote" style="padding: 10px 15px;margin-bottom: 0;">' +
            '<form id="customParameForm" class="layui-form"><div id="customParameDiv"></div></form>' +
            '<div class="layui-input-inline">' +
            '<button type="button" class="layui-btn importbtn" id="importfile">' + bt.lang("common.button.imports") + '</button>';
        if (!config.hideDownTempBtn) {
            html += '<a href="' + downUrl + '" class="layui-btn" style="margin-left:10px;"><i class="layui-icon">&#xe601;</i>' + bt.lang("common.button.templetDownLoad") + '</a>';
        }
        html += '</div></blockquote>' + tabhtml +
            '<div class="layui-form" id="view"></div>' +
            '</div>';
        importIndex= layer.open({
            type: 1,
            area: [config.width, config.height],//宽高
            maxmin: true,
            content: html,
            btn: [bt.lang("common.button.sure"), bt.lang("common.button.cancel")],
            full: function () { //解决最大化时，表格样式异常
                $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
            },
            yes: function (index, layero) {
                // 点击确认后，禁用3秒，并加遮罩层
                $(".layui-layer-btn0").addClass("layui-disabled");
                $(".layui-layer-btn0").prop('disabled', 'disabled');
                bt.loading.show();
                setTimeout(function () {
                    $(".layui-layer-btn0").removeClass("layui-disabled");
                    $(".layui-layer-btn0").removeAttr("disabled");
                }, 3000);
                selectData = [];
                var openIndex = index;
                if (saveData && saveData.length > 0) {
                    if (errorTotal > 0) {
                        if (AllheadMap && AllheadMap['table_top']) {
                            bt.loading.hide();
                            parent.layer ? parent.layer.msg(bt.lang("common.import.exist.error.data"), {icon: 7}) : layer.msg(bt.lang("common.import.exist.error.data"), {icon: 7});
                            return false;
                        }
                        bt.loading.hide();
                        var py = parent.layer ? parent.layer : layer;
                        py.confirm(bt.lang("common.message.hasError") + '?', function (index) {
                            checkFun(openIndex,that);
                        });
                    } else {
                        checkFun(index,that);
                    }
                } else {
                    bt.loading.hide();
                    parent.layer ? parent.layer.msg(bt.lang("common.import.no.qualified.data"), {icon: 7}) : layer.msg(bt.lang("common.import.no.qualified.data"), {icon: 7});
                    return false;
                }
            },
            btn2: function (index, layero) {
                saveData = [], headData = [], selectData = [];
            },
            cancel: function (index, layero) {
                saveData = [], headData = [], selectData = [];
            },
            success: function (layero) {
                $("body").addClass("excel_view_body");
                callBack && callBack();
                //表格切换时，刷新表格样式
                element.on('tab(docDemoTabBrief)', function (data) {
                    $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
                });
            },
            end: function () {
                //清除后台缓存
            }
        });

        $("input.layui-upload-file").hide();
    };
    //初始化事件
    BTImport.prototype.initEvent = function (config) {
        var that = this;
        var pageGetData = function (divId, key, pageNo, pageSize) {
            bt.loading.show();
            // 新增了服务前缀
            $.ajax({
                type: "GET",
                url: basePath + config.serverName+ "/import/excel/uploadPage?key=" + key + "&page=" + pageNo + "&len=" + pageSize,
               // dataType: "json",
               // contentType: "application/json",
                success: function (result) {
                    _pageNo = (pageNo - 1) * pageSize;
                    bt.loading.hide();
                    if (result) {
                        try {
                            //将小数位最后为0的转字符串
                            result = result.replace(regexZero, ":\"$1\",");
                            result = JSON.parse(result);
                            //var json = JSON.stringify(result);
                            //json = json.replace(regexZero,":\"$1\",");
                            //result = JSON.parse(json);
                        } catch (e) {
                            result = {};
                        }
                    }
                    // if (!result.success) {
                    //     parent.layer ? parent.layer.alert(result.msg, {icon: 2}) : layer.alert(result.msg, {icon: 2});
                    //     return;
                    // }
                    if (!LangMap || JSON.stringify(LangMap) === "{}") {
                        LangMap = result.list && result.list.length > 0 ? result.list[0][config.langMap] : {};
                    }

                    var gettpl = $("#" + importTplId).html();
                    var y_list = {
                        map: headData,
                        list: result.list,
                        alignment: result.alignment ? result.alignment : {},
                        split: result.split ? result.split : {},
                        pageNo: _pageNo
                    };
                    laytpl(gettpl).render(y_list, function (html) {
                        $("#" + divId).html(html);
                    });

                },
                error: function () {
                    bt.loading.hide();
                }
            });
        };
        var pstr = parseParam(config.params);
        var exts = "xls|xlsx";
        if (config.url.indexOf("uploadCustom") > 0) {
            exts += "|csv|txt";
        }
        layui.uploadExcel.render({
            elem: "#importfile",
            field: "file",
            accept: "file",
            exts: exts,
            url: config.url + '?page=' + (config.page ? 1 : 0) + '&code=' + config.code
                + '&titleRows=' + config.titleRows + '&headRows=' + config.headRows + '&endRows=' + config.endRows
                + (pstr ? "&" + pstr : "")
            , before: function (input) {
                bt.loading.show();
                $("#" + viewImportId + " .importbtn").addClass("layui-disabled");
            }, appendCustomParame: function (input) {
                return parseCustomParame();
            }
            , error: function (index, upload) {
                bt.loading.hide();
                $("#" + viewImportId + " .importbtn").removeClass("layui-disabled");
            }
            , done: function (res, index, upload) {
                bt.loading.hide();
                $("#" + viewImportId + " .importbtn").removeClass("layui-disabled");
                if (!res) return;
                if (res.success === false) {
                    saveData = [];
                    parent.layer ? parent.layer.alert(res.msg, {icon: 2}) : layer.alert(res.msg, {icon: 2});
                    return;
                }
                /*try{
                    res = res.replace(regexZero,":\"$1\",");//将小数位最后为0的转字符串
                    res = JSON.parse(res);
                    //var json = JSON.stringify(res);
                    //json = json.replace(regexZero,":\"$1\",");
                    //res = JSON.parse(json);
                }catch (e){
                    res = {};
                }*/
                AllheadMap = {}, LangMap = {}, errorTotal = 0, cacheKey = "", _cacheCode = "",summary = 0,errorSummary=0;
                var alignment = res.alignment ? res.alignment : {};
                var split = res.split ? res.split : {};
                _cacheCode = res.code ? res.code : "";
                if (res && res.map) {
                    saveData = [];
                    cacheKey = res.key ? res.key : "";
                    var templist = res.list ? res.list : [];
                    for (var e1 in templist) {
                        if (!templist[e1][config.errorMsg]) {
                            saveData.push(templist[e1]);
                            if(templist[e1]['money']) {
                                summary += templist[e1]['money']
                            }
                        }else {
                            if(templist[e1]['money']) {
                                errorSummary += templist[e1]['money']
                            }
                        }
                    }
                    headData = res.map;
                    if (cacheKey) {
                        errorTotal = res.total ? res.total : 0;
                        $("#viewPage").html(""), $("#errorviewPage").html("");
                        bt.page.init({
                            elem: '#viewPage',
                            lengthMenu:config.pageLengthMenu ? config.pageLengthMenu:[10,25,50,100],
                            len:config.pageLen ? config.pageLen:10,
                            count: res.allTotal ? res.allTotal : 0,
                            currentPage: 1,
                            jump: function (obj) {
                                if (!obj.len) {
                                    obj.len = 10;
                                }
                                pageGetData(viewImportId, cacheKey, obj.curr, obj.len);
                            }
                        });
                        bt.page.init({
                            elem: '#errorviewPage',
                            lengthMenu:config.pageLengthMenu ? config.pageLengthMenu:[10,25,50,100],
                            len:config.pageLen ? config.pageLen:10,
                            count: errorTotal,
                            currentPage: 1,
                            jump: function (obj) {
                                if (!obj.len) {
                                    obj.len = 10;
                                }
                                pageGetData(errorViewImportId, cacheKey + "_error", obj.curr, obj.len);
                            }
                        });
                        pageGetData(viewImportId, cacheKey, 1, 10), pageGetData(errorViewImportId, cacheKey + "_error", 1, 10);
                    } else {
                        var gettpl = $("#" + importTplId).html();
                        LangMap = saveData && saveData.length > 0 ? saveData[0][config.langMap] : {};
                        //laytpl(gettpl).render(res, function(html){
                        laytpl(gettpl).render({
                            map: headData,
                            list: saveData,
                            alignment: alignment,
                            split: split,
                            pageNo: 0
                        }, function (html) {
                            $("#" + viewImportId).html(html);
                        });
                        var elist = [];
                        for (var d in res.list) {
                            if (res.list[d][config.errorMsg]) {
                                elist.push(res.list[d]);
                            }
                        }
                        errorTotal = elist.length;
                        var e_list = {map: headData, list: elist, alignment: alignment, split: split, pageNo: 0};
                        laytpl(gettpl).render(e_list, function (html) {
                            $("#" + errorViewImportId).html(html);
                        });

                        if(that.options.summaryHtml){
                            // 导出汇总功能
                            var summaryHtmlObj = that.options.summaryHtml({
                                successData:saveData,
                                errorData:elist
                            })
                            $("#viewPage").addClass('import-summay').html(summaryHtmlObj.successHtml);
                            $("#errorviewPage").addClass('import-summay').html(summaryHtmlObj.errorHtml)
                        }
                    }
                } else {
                    saveData = [];
                    headData = [];
                    $("#" + viewImportId).html(""), $("#" + errorViewImportId).html("");
                    var headerData = res.list;
                    AllheadMap = res.header;
                    var headerMap = res.header['table_top'];
                    var templist = headerData ? headerData : [];
                    for (var e1 in templist) {
                        if (!templist[e1][config.errorMsg]) {
                            saveData.push(templist[e1]);
                        }
                    }
                    var head_o = {list: saveData, map: headerMap, alignment: alignment, split: split, pageNo: _pageNo};
                    //saveData.push(headerData);
                    headData.push(headerMap);
                    LangMap = saveData && saveData.length > 0 ? (saveData[0][config.langMap] ? saveData[0][config.langMap] : saveData[0][0][config.langMap]) : {};
                    var gettpl = $("#" + importTplId).html();
                    laytpl(gettpl).render(head_o, function (html) {
                        $("#" + viewImportId).append(html);
                        form.render('checkbox');
                    });
                    var errorTopList = [];
                    for (var e in headerData) {
                        if (headerData[e][config.errorMsg]) {
                            errorTopList.push(headerData[e]);
                        }
                    }
                    if (errorTopList.length > 0) {
                        errorTotal = errorTopList.length;
                        laytpl(gettpl).render({
                            list: errorTopList,
                            map: headerMap,
                            alignment: alignment,
                            split: split,
                            pageNo: 0
                        }, function (html) {
                            $("#" + errorViewImportId).append(html);
                        });
                    }
                    var map = res.header;
                    for (var i = 0; i < res.list.length; i++) {//只有一个
                        var o = res.list[i];
                        for (var key in o) {//只有一个
                            if (map[key]) {
                                var list = [];
                                headData.push(map[key]);
                                if (isArray(o[key])) {
                                    list = o[key];
                                } else {
                                    list.push(o[key]);
                                }
                                var childtemplist = [];
                                for (var e2 in list) {
                                    if (!list[e2][config.errorMsg]) {
                                        childtemplist.push(list[e2]);
                                    }
                                }
                                var head_o = {
                                    list: childtemplist,
                                    map: map[key],
                                    alignment: alignment,
                                    split: split,
                                    pageNo: 0
                                };
                                laytpl(gettpl).render(head_o, function (html) {
                                    $("#" + viewImportId).append(html);
                                    form.render('checkbox');
                                });
                                var elist1 = [];
                                for (var d1 in list) {
                                    if (list[d1][config.errorMsg]) {
                                        elist1.push(list[d1]);
                                    }
                                }
                                if (!errorTotal) {
                                    errorTotal = elist1.length;
                                }
                                var e_list1 = {
                                    map: map[key],
                                    list: elist1,
                                    alignment: alignment,
                                    split: split,
                                    pageNo: 0
                                };
                                laytpl(gettpl).render(e_list1, function (html) {
                                    $("#" + errorViewImportId).append(html);
                                });
                            }
                        }
                    }
                }
                if (!cacheKey) {
                    that.setTable();
                }
                if(!config.isShowData && saveData && saveData.length > 0){
                    checkFun(importIndex,btImport);
                }
            }
        });
        element.on('tab(docDemoTabBrief)', function (data) {
            $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
        })
        //全选、反选
        /*form.on('checkbox('+allImportCheckName+')', function(data){
            $(data.elem).parents('table').find('tbody input[type="checkbox"]').each(function(index, item){
                item.checked = data.elem.checked;
            });
            form.render('checkbox');
        });
        //子复选框的事件
        form.on('checkbox('+itemImportCheckName+')', function(data){
            var allcheck = $(data.elem).parents('table').find('thead input[type="checkbox"]');
            allcheck && (allcheck[0].checked = ($(data.elem).parents('table').find('tbody input[type="checkbox"]').length==$(data.elem).parents('table').find('tbody input[type="checkbox"]:checked').length));
            form.render('checkbox');
        });
        //点击td
        $("#"+viewImportId).on('click','td.'+tdImportCheckName, function(){
            var allcheck = $("#"+viewImportId+" table").find('thead input[type="checkbox"]');
            var tdcheck = $(this).parent('tr').find('input[type="checkbox"]');
            if(tdcheck[0]){
                tdcheck[0].checked = !tdcheck[0].checked;
                allcheck && (allcheck[0].checked = ($("#"+viewImportId+" table").find('tbody input[type="checkbox"]').length==$("#"+viewImportId+" table").find('tbody input[type="checkbox"]:checked').length));
                form.render('checkbox');
            }
        });*/
        if (config.customParameUrl) {
            // 加载自定义参数页面
            $.ajax({
                url: config.customParameUrl,
                type: 'get',
                dataType: 'html',
                data: {},
                success: function (data) {
                    $("#customParameDiv").html(data);
                }
            });
        }
    };
    BTImport.prototype.setTableConfig = function (type) {
        var that = this;
        var dconfig = {
            language: lang,
            dom: that.options.hideExportBtn?'rt<"wrapper datatable-page">':'Brt<"wrapper datatable-page">',
            ordering: true,
            serverSide: false,
            scrollX: '600px',
            scrollY: '',
            lengthMenu: that.options.pageLengthMenu?that.options.pageLengthMenu:[10, 25, 50, 100],
            displayLength:that.options.pageLen?that.options.pageLen:10,
            paging: true,
            colReorder: true,
            buttons: [{
                extend: 'collection',
                text: bt.lang('common.button.exports'),
                buttons: [
                    {
                        extend: 'excel',
                        text: bt.lang('common.button.exportsCurrentPage'),
                        className: 'active',
                        filename: type === 1 ? "exportData" : "currentErrorData",
                        title: null,
                        exportOptions: {
                            modifier: {
                                page: 'current'
                            },
                            columns: type === 1 ? ":not(:first):not(:last)" : ":not(:first)",
                            type: "current"
                        }
                    },
                    {
                        extend: 'excel',
                        text: bt.lang('common.button.exportsAll'),
                        className: 'output active',
                        filename: type === 1 ? "allData" : "allErrorData",
                        title: null,
                        exportOptions: {
                            modifier: {
                                page: 'all'
                            },
                            columns: type === 1 ? ":not(:first):not(:last)" : ":not(:first)"
                        }
                    }
                ],
                fade: true
            }],
            initComplete: function (settings) {
                if (!$(this).parents('.dataTables_scroll').find('.wrapper').length) {
                    that.setTableInformation(settings, $(this), type);//设置表格底部翻页功能
                    //var wrapper = $(this).parents('.import-tablecss').find('.wrapper');
                    //$(this).parents('.dataTables_scroll').append(wrapper);
                }
            },
            drawCallback: function (settings) {
                updatePages($(this), settings);
                if (!$(this).find('table td.dataTables_empty').length) {
                    $(this).find('.wrapper').css('border-top', '1px solid #d2d2d2');
                } else {
                    $(this).find('.wrapper').css('border-top', 'none');
                }
            }
        };
        return dconfig;
    };
    BTImport.prototype.setTable = function () {
        var that = this;
        // 获取产品的肤色
        if(store.get('web-skin') == 'dark-skin'){
            $('.import_excel_common #data_tab table').addClass('table-dark')
            $('.import_excel_common #error_data_tab table').addClass('table-dark')
            $('#data_tab').addClass('dark-skin');
            $('#error_data_tab').addClass('dark-skin');
        }
        $('.import_excel_common #data_tab table').DataTable(that.setTableConfig(1));
        $('.import_excel_common #error_data_tab table').DataTable(that.setTableConfig(2));
        var tables = $('.import_excel_common table td.dataTables_empty');
        $.each(tables, function () {
            $(this).parents("div.dataTables_scrollBody").addClass("dataTablesNoHeight");
        })

        $.fn.dataTable.tables({visible: true, api: true}).columns.adjust();
    };
    BTImport.prototype.setTableInformation = function (settings, $table, tableType) {
        var that = this;
        var lengthMenu = that.options.pageLengthMenu?that.options.pageLengthMenu:[10, 25, 50, 100];//每页显示多少条信息，array
        var table = new $.fn.dataTable.Api(settings);
        //获取表格的当前页码(page)、所有页码(pages)、数据显示的条数(recordsDisplay)，所有条数(recordsTotal)、每页条数（length），起始行索引（start）
        var infoObj = table.page.info();
        var len = table.page.len();
        var toolsDiv = $table.parents('.dataTables_scroll').siblings('.wrapper');
        var hoverMouse = infoObj.page === 0 ? 'firstPage' : 'table-jumpPage';
        var lastMouse = (infoObj.pages === 0 || infoObj.pages === 1) ? 'firstPage' : 'table-jumpPage';
        //重置总页码/起始页
        var pages = infoObj.pages === 0 ? 1 : infoObj.pages;
        // pages = Math.ceil( infoObj.end / len);
        var start = infoObj.end === 0 ? 0 : infoObj.start + 1;
        //页码html
        var html = '';
        html += '<div class="table-page">';
        html += '<span class="table-firstPage pageLeft pages span fa fa-fw fa-step-backward spanLeft ' + hoverMouse + '" title="First Page" ></span>';
        html += '<span class="table-prevPage pageLeft pages span fa fa-fw fa-backward spanLeft ' + hoverMouse + '" title="Previous Page"></span>';
        html += '<span class="text-explode">|</span>';
        html += '<span class="table-pageInput span"><span style="margin-right: 5px">' + bt.lang("common.table.the") + '</span>';
        html += '<input class="table-pageNumber" value="' + (infoObj.page + 1) + '" > of';
        html += '<span class="pd2 totalPage" style="padding-left: 5px;max-width: 45px;">' + pages + '</span></span>';
        html += '<span class="text-explode">|</span>';
        html += '<span class="table-nextPage pageRight pages span fa fa-fw fa-forward ' + lastMouse + '" title="Next Page"></span>';
        html += '<span class="table-lastPage pageRight pages span fa fa-fw fa-step-forward ' + lastMouse + '" title="Last Page"></span>';
        html += '<select lay-ignore class="table-length" style="display: inline-block">';
        for (var i = 0; i < lengthMenu.length; i++) {
            if (len === lengthMenu [i]) {
                html += '<option selected>';
            } else {
                html += '<option>';
            }
            html += lengthMenu[i] + '</option>';
        }
        html += '</select>';
        html += '</div>';
        //数据条数html
        var recordHtml = '';
        recordHtml += '<div class="table-record fr">';
        recordHtml += '<span class="pd2 pagecount">' + bt.lang("common.button.view") + '</span>';
        recordHtml += '<span class="table-startRecord pagecount">' + start + '</span>';
        recordHtml += '<span class="pd2 pagecount">-</span>';
        recordHtml += '<span class="table-endRecord pagecount">' + (infoObj.end) + '</span>';
        recordHtml += '<span class="pd2 pagecount">' + bt.lang("common.table.records") + '</span>,';
        recordHtml += '<span class="pd2">共</span>';
        recordHtml += '<span class="table-totalRecord">' + infoObj.recordsDisplay + '</span>';
        recordHtml += '<span class="pd2">' + bt.lang("sys.config.appPageSize.unit") + '</span>';
        if (that.options.sumMoney) {
            recordHtml += '<span class="pd2">，合计</span>';
            if (tableType == 1) {
                recordHtml += '<span class="table-sumRecord" style="margin-right: 10px">' + moneyFun.format(summary) + '</span>';
            }else if (tableType == 2) {
                recordHtml += '<span class="table-sumRecord" style="margin-right: 10px">' + moneyFun.format(errorSummary) + '</span>';
            }
            //recordHtml += '<span class="pd2">元 </span>';
        }
        toolsDiv.append(recordHtml);
        toolsDiv.append(html);
        that.setTableInformationEvent($table, settings)
    };
    //表格底部页码初始化注册方法
    BTImport.prototype.setTableInformationEvent = function ($table, settings) {
        tablePageInput();//保证输入的是正整数
        tableJumpPageEnter($table, settings); //注册回车跳转事件
        tableJumpPageClick($table, settings);//注册点击跳转事件
        tableClickLengthMenu($table, settings)//注册点击下拉框事件
    };

    function updatePages(elem, setting) {
        var wraper = elem.parents('.dataTables_scroll').siblings('.wrapper');
        var table = new $.fn.dataTable.Api(setting);
        var infoObj = table.page.info();
        wraper.find('.table-pageInput .table-pageNumber').val(infoObj.pages);
        wraper.find('.table-record .table-endRecord').text(infoObj.end);
        wraper.find('.table-record .table-totalRecord').text(infoObj.recordsDisplay);
        //如果重新查询之后，将回到第一页，所以需要更新如下数据
        if (infoObj.start === 0 && wraper[0] && wraper.find('.table-record .table-startRecord').text() !== 1) {
            wraper.find('.table-record .table-startRecord').text(infoObj.start + 1);
            wraper.find('.table-pageNumber').val(1);
            updatePageStyle(wraper.find('.table-pageInput span'), 0, table);
        }
    }

    //控制只能输入正整数
    function tablePageInput() {
        $('.dataTables_wrapper .wrapper input').on('keyup', function () {
            if (this.value.length === 1) {
                this.value = this.value.replace(/[^1-9]/g, '')
            } else {
                this.value = this.value.replace(/\D/g, '')
            }
        }).on('afterpaste', function () {
            if (this.value.length === 1) {
                this.value = this.value.replace(/[^1-9]/g, '0')
            } else {
                this.value = this.value.replace(/\D/g, '')
            }
            ;
        })
    }

    //按回车键页面跳转
    function tableJumpPageEnter($table, settings) {
        $("body").on('keydown', $table.parents(".dataTables_wrapper").find('.table-pageNumber'), function (e) {
            e = event || windw.event;
            if (e.keyCode === 13) {
                var val = parseInt($(this).val()) - 1;
                if (val >= 0) {
                    var table = new $.fn.dataTable.Api(settings);
                    table.page(val).draw('page');
                    updateRecord(table, $(this));//更新表格下方显示起止信息条数
                    updatePageStyle($(this), val, table);//更新首页码样式
                }
                $(this).blur();
            }
        })
    }

    //翻页跳转
    function tableJumpPageClick($table, settings) {
        $table.parents(".dataTables_wrapper").find('.wrapper').on('click', '.table-jumpPage', function () {
            var title = $(this).attr('title');
            var table = new $.fn.dataTable.Api(settings);
            var pageInputWrapper = $(this).siblings('.table-pageInput');
            var currentPage = pageInputWrapper.find('input').val() - 0 - 1;
            var totalPage = pageInputWrapper.find('.totalPage').text() - 0 - 1;
            var pageNumber = 0;
            switch (title) {
                case 'First Page':
                    pageNumber = 0;
                    break;
                case 'Previous Page':
                    pageNumber = currentPage - 1;
                    break;
                case 'Next Page':
                    if (currentPage < totalPage) {
                        pageNumber = currentPage + 1;
                    } else {
                        pageNumber = currentPage;
                    }
                    break;
                case 'Last Page':
                    pageNumber = totalPage;
                    break;
                default:
                    break;
            }
            table.page(pageNumber).draw('page');
            pageInputWrapper.find('input').val(pageNumber + 1);
            updatePageStyle($(this), pageNumber, table);//更新首页码样式
            updateRecord(table, $(this));//更新表格下方显示起止信息条数
        })
    }

    //点击下拉框选择每页显示多少条数据
    function tableClickLengthMenu($table, settings) {
        $table.parents(".dataTables_wrapper").find('.wrapper').on('change', '.table-length', function () {
            var value = $(this).val();
            var table = new $.fn.dataTable.Api(settings);
            table.page.len(value).draw();
            $(this).siblings('.table-pageInput').find('input').val(1);//重新选择时，回到第一列车
            updateRecord(table, $(this));//更新表格下方显示起止信息条数
            updatePageStyle($(this), 0, table);
        })
    }

    //更新表格下方显示起止信息条数
    function updateRecord($table, elem) {
        //获取表格的当前页码(page)、所有页码(pages)、数据显示的条数(recordsDisplay)，所有条数(recordsTotal)、每页条数（length），起始行索引（start）
        var infoObj = $table.page.info();
        var wrapper = elem.parents('.wrapper');
        if (infoObj) {
            var pages = 0;
            if (infoObj.pages === 0) {
                pages = 1;
            } else {
                pages = infoObj.pages;
            }
            var start = 0;
            if (infoObj.start === 0) {
                start = 1;
            } else {
                start = infoObj.start - 0 + 1;
            }
            var end = 0;
            if (infoObj.end === 0) {
                start = 0;
            } else {
                end = infoObj.end;
            }
            wrapper.find('.table-record .table-startRecord').text(start);
            wrapper.find('.table-record .table-endRecord').text(end);
            wrapper.find('.table-pageInput .totalPage').text(pages);
            wrapper.find('.table-record .table-totalRecord').text(infoObj.recordsDisplay);
        }
    }

    //更新首页或前一页的样式
    function updatePageStyle(elem, number, table) {
        number += 1;
        var infoObj = table.page.info();
        var pages = infoObj.pages;
        var wrapper = elem.parents('.wrapper');
        wrapper.find('.pages').addClass('firstPage').removeClass('table-jumpPage');
        if (pages === 1 || pages === 0) {
            return;
        } else {
            if (number !== 1) {
                wrapper.find('.pageLeft').removeClass('firstPage').addClass('table-jumpPage');
                if (number !== pages) {
                    wrapper.find('.pageRight').removeClass('firstPage').addClass('table-jumpPage');
                }
            } else {
                wrapper.find('.pageRight').removeClass('firstPage').addClass('table-jumpPage');
            }
        }
    }

    BTImport.prototype.init = function (options) {
        var that = this;
        // 新增：标记用户是否显式配置了上传url（显式配置则优先使用，不被后端覆盖）
        that._explicitUrl = !!(options && options.url);
        $.extend(true, this.options, options);
        var _config = that.options;
        var code = _config.code;
        if (code === undefined) {
            new Error('Import Error:' + bt.lang("common.import.param.code.error"));
        }
        // 获取上传文件的url，并且初始化插件
        that.getImportUrl(code);
        saveData = [];
        headData = [];
        return this;
    };
    // 获取上传文件的url
    BTImport.prototype.getImportUrl = function(code){
        var that = this;
        $.get(basePath + "/import/excel/getUrlByCode?code="+code, function (res) {
            if (res&&res.success) {
                if(!that._explicitUrl){
                    that.options.url = res.obj;
                    that.options.serverName = (res.attributes && res.attributes.prefix) || '';
                }
                //初始化插件
                that.initHtml(that.options, function () {
                    that.initEvent(that.options);
                });
            }
        },'JSON');
    }

    BTImport.prototype.getData = function () {
        return saveData;
    };
    BTImport.prototype.getSelectData = function () {
        return selectData;
    };
    BTImport.prototype.getHeadRows = function () {
        var data = [];
        if (headData) {
            for (var k in headData) {
                data.push(headData[k]);
            }
        }
        return data;
    };
    BTImport.prototype.success = function (callBack) {
        callBackFun = callBack;
    };
    BTImport.prototype.getCacheData = function (callBack) {
        callBackFun = callBack;
    };

    var btImport;
    var obj = {
        init: function (ops) {
            btImport = new BTImport();
            btImport.init(ops);
            return this;
        },
        getData: function () {
            return btImport.getData();//返回所有导入数据
        },
        getSelectData: function () {
            return btImport.getData();
            //return btImport.getSelectData();//返回所有选中数据
        },
        closeWindown: function () {
            $('.import_excel_common #data_tab .dataTables_scrollBody table').DataTable().destroy(true);
            $('.import_excel_common #error_data_tab .dataTables_scrollBody table').DataTable().destroy(true);
            layer.close(windownIndex);
        },
        getCacheData: function () {
            return btImport.getCacheData();
        },
        getHeadRows: function () {
            return btImport.getHeadRows();//返回表头集合
        },
        success: function (callBack) {
            btImport.success(callBack);
            return this;
        }
    };
    exports("btImport", obj)
});
importext.addcss('../../import/css/importExcel.css', 'importExcel');
