/**
 * Created by Fine on 2016/12/4.
 */

var crossTable = {
    table: function (args) {
        var data = args.data,
            chartConfig = args.chartConfig,
            tall = args.tall,
            pageDataNum = 20,
            drill = args.drill,
            random = Math.random().toString(36).substring(2),
            container = args.container;
        var html = "<table class = 'table_wrapper' id='tableWrapper" + random + "'><thead class='fixedHeader'>",
            colContent = "<tr>";

        // valueCount: 指标元素的个数
        var valueCount = chartConfig.values[0] && chartConfig.values[0].cols && chartConfig.values[0].cols.length;
        // groupCount: 列维元素的个数
        var groupCount = chartConfig.groups.length;
        // 是否需要调换指标列表头标题的位置：当指标列表头标题都相同，可调换位置。
        var needChangeValueTitlePosition = valueCount == 1 && groupCount > 0;
        /*
            created by chenlw on 2019/11/07
            列表头的渲染算法：
            （1）先处理列维的列表头标题，然后再处理指标的列表头标题。
         */
        // 渲染列维的列表头标题
        for (var i = 0; i < chartConfig.groups.length; i++) {
            var groupId = chartConfig.groups[i].id;
            // colspan用于合并值相同的列表头单元格
            var colspan = 1;
            var colList = [];

            if (needChangeValueTitlePosition && i == chartConfig.groups.length - 1) {
                for (var t = 0; t < chartConfig.keys.length; t++) {
                    // 指标列表头标题调换位置后，需要把行维的标题头调换
                    colContent += "<th class='header_key'><div>" + data[groupCount][t].data + "</div></th>";
                }
            } else {
                for (var t = 0; t < chartConfig.keys.length; t++) {
                    // 列维元素的单元格不显示。
                    colContent += "<th class=" + data[i][t].property + "><div></div></th>";
                }
            }

            for (var y = chartConfig.keys.length; y < data[i].length; y++) {
                if ((data[i][y + 1]) && (data[i][y].data == data[i][y + 1].data)) {
                    if (i > 0) {
                        // 判断相邻列表头的值是否相同，如果相同则colSpan递增。
                        var noEqual = false;
                        for (var s = i - 1; s > -1; s--) {
                            if (data[s][y].data != data[s][y + 1].data) {
                                noEqual = true;
                                break;
                            }
                        }
                        if (noEqual) {
                            colList.push({
                                data: data[i][y].data,
                                colSpan: colspan,
                                property: data[i][y].property
                            });
                            colspan = 1;
                        } else {
                            // 相邻列表头的值相同，colSpan递增，用于合并列表头的单元格
                            colspan++;
                        }
                    }
                    else if (i == 0) {
                        colspan++;
                    }
                }
                else {
                    data[i][y] != data[i][y - 1]
                        ? colList.push({
                            data: data[i][y].data,
                            colSpan: colspan,
                            property: data[i][y].property
                        })
                        : null;
                    colspan = 1;
                }
            }
            for (var c = 0; c < colList.length; c++) {
                var d = "";
                // 拼接上卷/下钻的节点
                if (drill && drill.config[groupId] && (drill.config[groupId].down || drill.config[groupId].up)) {
                    d += "class='table_drill_cell'";
                    if (drill.config[groupId].down) {
                        d += " drill-down='" + groupId + "' ";
                    }
                    if (drill.config[groupId].up) {
                        d += " drill-up='" + groupId + "' ";
                    }
                }
                var value = "<div " + d + ">" + colList[c].data + "</div>";
                // 通过colSpan合并相同的单元格
                colContent += colList[c].colSpan > 1 ? "<th colspan=" + colList[c].colSpan +
                    " class=" + colList[c].property + ">" + value + "</th>" :
                    "<th class=" + colList[c].property + ">" + value + "</th>";
            }
            colContent += "</tr>";
        }

        // 渲染指标列的列表头标题：最初的处理逻辑
        // data[chartConfig.groups.length].length是指标在data二维数组的所在的下标值
        // colContent = colContent + '<tr>';
        // for (var k = 0; k < data[chartConfig.groups.length].length; k++) {
        //     colContent += "<th class=" + data[chartConfig.groups.length][k].property + "><div>" + data[chartConfig.groups.length][k].data + "</div></th>";
        // }
        // colContent = colContent + '</tr>';

        // 当指标列表头标题都相同，需要对列表头标题进行合并
        if (needChangeValueTitlePosition) {
            var valueTitleContent = '<tr>';
            // 渲染行维的标题头
            var keyCount = chartConfig.keys.length;
            for (var k = 0; k < keyCount; k++) {
                valueTitleContent += "<th class='header_empty'><div></div></th>";
            }
            // 合并指标列的标题单元格
            valueTitleContent += "<th class=" + data[chartConfig.groups.length][keyCount].property + " colspan=" + (data[chartConfig.groups.length].length - keyCount) + "><div>"
                + data[chartConfig.groups.length][keyCount].data + "</div></th>";
            valueTitleContent += '</tr>';
            colContent = valueTitleContent + colContent;

            // 调换data指标列表头数组和列维列表头数组的位置
            var changeRowNum = groupCount + 1;
            var valueTitleArr = data[groupCount];
            for (var m = changeRowNum - 1; m >= 1; m--) {
                data[m] = data[m - 1];
            }
            data[0] = valueTitleArr;
            for (var t = 0; t < chartConfig.keys.length; t++) {
                data[groupCount][t] = {
                    property: 'header_key',
                    data: data[0][t].data
                };
                data[0][t] = {
                    property: 'header_empty',
                    data: ''
                };
            }
        } else {
            // 渲染指标列的列表头标题
            // data[chartConfig.groups.length].length是指标在data二维数组的所在的下标值
            colContent = colContent + '<tr>';
            for (var k = 0; k < data[chartConfig.groups.length].length; k++) {
                colContent += "<th class=" + data[chartConfig.groups.length][k].property + "><div>" + data[chartConfig.groups.length][k].data + "</div></th>";
            }
            colContent = colContent + '</tr>';
        }


        // 渲染表格主体
        html += colContent + "</thead><tbody class='scrollContent'>";
        // headerLines，列表头的行数
        var headerLines = chartConfig.groups.length + 1;
        // dataPage：分页数据
        var dataPage = this.paginationProcessData(data, headerLines, pageDataNum);
        // colNum：一共有多少列。
        var colNum = data[0].length;
        // rowNum：一共有多少行数据
        var rowNum = colNum ? data.length - headerLines : 0;

        // 渲染行数据
        var trDom = this.render(dataPage[0], chartConfig, drill);
        html = html + trDom + "</tbody></table>";

        // var operate = "<div class='pull-left toolbar" + random + "'>" + "<span class='exportBnt' title='" + cboardTranslate("CROSS_TABLE.EXPORT") + "'></span></div>";
        var operate = "<div class='pull-left toolbar" + random + "'>" + "<button class='btn btn-link' class='exportButton' title='" + cboardTranslate("CROSS_TABLE.EXPORT") + "'>导出表格</button></div>";

        // 分页数目选择DOM
        var optionDom = "<select><option value='20'>20</option><option value='50'>50</option><option value='100'>100</option><option value='150'>150</option></select>";
        var p_class = "p_" + random;
        var PaginationDom = "<div style='margin-top: 10px;' class='" + p_class + "'>";

        PaginationDom += "<div class='optionNum'>";
        PaginationDom += "<span style='margin-left: 10px;'>" + cboardTranslate("CROSS_TABLE.SHOW") + "</span>" + optionDom + "<span>" + cboardTranslate("CROSS_TABLE.ENTRIES") + "</span></div>";

        PaginationDom += "<div class='page'><ul></ul>";
        PaginationDom += "</div>";

        // var operate = "<div class='toolbar toolbar" + random + "'><span class='info'><b>info: </b>" + rowNum + " x " + colNum + "</span>" +
        //     "<span class='exportBnt' title='" + cboardTranslate("CROSS_TABLE.EXPORT") + "'></span></div>";
        // var operate = "<div class='toolbar toolbar" + random + "'>" +
        //     "<span class='exportBnt' title='" + cboardTranslate("CROSS_TABLE.EXPORT") + "'></span></div>";
        // $(container).html(operate);

        $(container).html("");
        $(container).append("<div class='tableView table_" + random + "' style='width:99%;max-height:" + tall + "px;overflow:auto'>" + html + "</div>");
        $(container).append(PaginationDom);
        var pageObj = {
            data: dataPage,
            chartConfig: chartConfig,
            drill: drill
        };
        // 数据不为空则渲染分页DOM
        data.length ?
            this.renderPagination(dataPage.length, 1, pageObj, $('.' + p_class + ' .page>ul')[0])
            : null;

        // 注册分页DOM的监听事件
        this.clickPageNum(dataPage, chartConfig, drill, p_class);
        this.clickNextPrev(dataPage.length, pageObj, p_class);
        // 监听分页显示数目切换事件
        this.selectDataNum(data, chartConfig.groups.length + 1, chartConfig, drill, p_class, "table_" + random, args.render);

        // 监听表格导出事件
        // this.export(random, data, args.chartConfig.chartName);
        // this.exportByChartId(random, data, args.chartConfig.chartName, args.chartConfig.chartId);
        this.exportByExportDomElementId(args.chartConfig.exportDomElementId, data, args.chartConfig.chartName);
    },
    bandDrillEvent: function (t_class, drill, render) {
        $('.' + t_class + ' .table_drill_cell[drill-down]').click(function(){
            var down = $(this).attr('drill-down');
            var value = $(this).html();
            drill.drillDown(down, value, render);
        });
        $.contextMenu({
            selector: '.' + t_class + ' .table_drill_cell',
            build: function ($trigger, e) {
                var down = $trigger.attr('drill-down');
                var up = $trigger.attr('drill-up');
                var value = $trigger.html();
                var items = {};
                if (up) {
                    items.up = {name: cboardTranslate("COMMON.ROLL_UP"), icon: "fa-arrow-up"}
                }
                if (down) {
                    items.down = {name: cboardTranslate("COMMON.DRILL_DOWN"), icon: "fa-arrow-down"}
                }
                return {
                    callback: function (key, options) {
                        if ('up' == key) {
                            drill.drillUp(up, render);
                        } else if ('down' == key) {
                            drill.drillDown(down, value, render);
                        }
                    },
                    items: items
                };
            }
        });

    },
    paginationProcessData: function (rawData, headerLines, pageSize) {
        var dataLength = rawData.length - headerLines;
        var lastPageLines = dataLength % pageSize;
        var fullSizePages = parseInt(dataLength / pageSize);
        var totalPages;
        lastPageLines == 0 ? totalPages = fullSizePages : totalPages = fullSizePages + 1;
        var pageData = [];
        for (var currentPage = 1; currentPage < totalPages + 1; currentPage++) {
            var startRow = (currentPage - 1) * pageSize + headerLines;
            var partData = rawData.slice(startRow, startRow + pageSize);
            pageData.push(partData);
        }
        return pageData;
    },
    render: function (data, chartConfig, drill) {
        var html = '';
        if (data === undefined) {
            return html;
        }
        for (var r = 0; r < chartConfig.keys.length; r++) {
            for (var n = 1; n < data.length; n++) {
                var node = data[n][r].data;
                if (r > 0) {
                    var parent = data[n][r - 1].data;
                    var next;
                    n > 0 ? next = data[n - 1][r - 1].data : null;
                    (node == data[n - 1][r].data && parent == next) ? data[n][r] = {
                        data: data[n][r].data,
                        // rowSpan: 'row_null',
                        rowSpan: 'row',
                        property: data[n][r].property
                    } : data[n][r] = {
                        data: data[n][r].data,
                        rowSpan: 'row',
                        property: data[n][r].property
                    };
                }
                else if (r == 0) {
                    var preNode = n > 0 ? data[n - 1][r].data : null;
                    (node == preNode) ? data[n][r] = {
                        data: data[n][r].data,
                        //rowSpan: 'row_null',
                        rowSpan: 'data',
                        property: data[n][r].property
                    } : data[n][r] = {
                        data: data[n][r].data,
                        rowSpan: 'row',
                        property: data[n][r].property
                    };
                }
            }
        }
        for (var n = 0; n < data.length; n++) {
            var rowContent = "<tr>";
            var isFirstLine = (n == 0) ? true : false;
            for (var m = 0; m < chartConfig.keys.length; m++) {
                var currentCell = data[n][m];
                var rowParentCell = data[n][m - 1];
                var cur_data = currentCell.data ? currentCell.data : "";
                var keyId = chartConfig.keys[m].id;
                var align = chartConfig.keys[m].align;
                if (drill && drill.config[keyId] && (drill.config[keyId].down || drill.config[keyId].up)) {
                    var d = "";
                    if (drill.config[keyId].down) {
                        d += " drill-down='" + keyId + "' ";
                    }
                    if (drill.config[keyId].up) {
                        d += " drill-up='" + keyId + "' ";
                    }
                    cur_data = "<div class='table_drill_cell' " + d + ">" + cur_data + "</div>";
                }
                if (m > 0) {
                    if (currentCell.rowSpan == 'row_null' && rowParentCell.rowSpan == 'row_null' && !isFirstLine) {
                        // rowContent += "<th class=row_null><div></div></th>";
                        rowContent += "<th class=row><div></div></th>";
                    } else {
                        rowContent += "<th style='text-align:"+align+";padding:2px 5px;' class=row><div>" + cur_data + "</div></th>";
                    }
                } else {
                    if (currentCell.rowSpan == 'row_null' && !isFirstLine) {
                        //rowContent += "<th class=row_null><div></div></th>";
                        rowContent += "<th class=row><div></div></th>";
                    } else {
                        rowContent += "<th style='padding:2px 5px;text-align:"+align+"' class=row><div>" + cur_data + "</div></th>";
                    }
                }
            }
            for (var y = chartConfig.keys.length; y < data[n].length; y++) {
                var align = chartConfig.values[0].cols[(y-chartConfig.keys.length)%chartConfig.values[0].cols.length].align;
                rowContent += "<td style='padding:2px 5px;text-align:"+align+"'class=" + data[n][m].property + "><div>" + data[n][y].data + "</div></td>";
            }
            html = html + rowContent + "</tr>";
        }
        return html;
    },
    selectDataNum: function (data, num, chartConfig, drill, random, t_class, render) {
        var _this = this;
        $('.' + random).on('change', '.optionNum select', function (e) {
            var pageDataNum = e.target.value;
            var dataPage = _this.paginationProcessData(data, num, pageDataNum);

            var dom = $(e.target.offsetParent).find('.page>ul')[0];
            var tbody = $(e.target.offsetParent).find('tbody')[0];
            tbody.innerHTML = (_this.render(dataPage[0], chartConfig, drill));
            _this.renderPagination(dataPage.length, 1, null, dom);
            $('.' + random).off('click');
            _this.clickPageNum(dataPage, chartConfig, drill, random);
            var pageObj = {
                data: dataPage,
                chartConfig: chartConfig,
                drill: drill
            };
            _this.clickNextPrev(dataPage.length, pageObj, random);
        });
        _this.bandDrillEvent(t_class, drill, render);
    },
    clickPageNum: function (data, chartConfig, drill, random) {
        var _this = this;
        $('.' + random).on('click', 'a.pageLink', function (e) {
            var pageNum = e.target.innerText - 1;
            var pageObj = {
                data: data,
                chartConfig: chartConfig,
                drill: drill
            };

            var dom = $(e.target.offsetParent).find('.page>ul')[0];
            var tbody = $(e.target.offsetParent).find('tbody')[0];
            tbody.innerHTML = _this.render(data[pageNum], chartConfig, drill);
            _this.renderPagination(data.length, parseInt(e.target.innerText), pageObj, dom);
        });
    },
    renderPagination: function (pageCount, pageNumber, pageObj, target) {
        if (pageCount == 1) {
            return  target.innerHTML = '';
        }
        var liStr = '<li><a class="previewLink">' + cboardTranslate("CROSS_TABLE.PREVIOUS_PAGE") + '</a></li>';
        if (pageCount < 10) {
            for (var a = 0; a < pageCount; a++) {
                liStr += '<li><a class="pageLink">' + (a + 1) + '</a></li>';
            }
        }
        else {
            if (pageNumber < 6) {
                for (var a = 0; a < pageNumber + 2; a++) {
                    liStr += '<li><a class="pageLink">' + (a + 1) + '</a></li>';
                }
                liStr += '<li class="disable"><span class="ellipse">...</span></li>';
                for (var i = pageCount - 2; i < pageCount; i++) {
                    liStr += '<li><a class="pageLink">' + (i + 1) + '</a></li>';
                }
            } else if (pageNumber <= (pageCount - 5)) {
                for (var c = 0; c < 2; c++) {
                    liStr += '<li><a class="pageLink">' + (c + 1) + '</a></li>';
                }
                liStr += '<li class="disable"><span class="ellipse">...</span></li>';
                for (var j = pageNumber - 2; j < pageNumber + 3; j++) {
                    liStr += '<li><a class="pageLink">' + j + '</a></li>';
                }
                liStr += '<li class="disable"><span class="ellipse">...</span></li>';
                for (var i = pageCount - 2; i < pageCount; i++) {
                    liStr += '<li><a class="pageLink">' + (i + 1) + '</a></li>';
                }
            } else {
                for (var c = 0; c < 2; c++) {
                    liStr += '<li><a class="pageLink">' + (c + 1) + '</a></li>';
                }
                liStr += '<li class="disable"><span class="ellipse">...</span></li>';
                for (var i = pageNumber - 2; i < pageCount + 1; i++) {
                    liStr += '<li><a class="pageLink">' + i + '</a></li>';
                }
            }
        }
        liStr += '<li><a class="nextLink">' + cboardTranslate("CROSS_TABLE.NEXT_PAGE") + '</a></li>';
        if (target) {
            target.innerHTML = liStr;
            if (pageNumber == 1) {
                target.childNodes[0].setAttribute('class', 'hide');
            } else if (pageNumber == pageCount) {
                target.childNodes[target.childNodes.length - 1].setAttribute('class', 'hide');
            }
            this.buttonColor(pageNumber, target);
        }
        // else {
        //     $('.page>ul').html(liStr);
        //     if (pageNumber == 1) {
        //         $('.page a.previewLink').addClass('hide');
        //     } else if (pageNumber == pageCount) {
        //         $('.page a.nextLink').addClass('hide');
        //     }
        //     this.buttonColor(pageNumber);
        //     this.clickNextPrev(pageCount, pageObj);
        // }
    },
    buttonColor: function (pageNum, target) {
        if (target) {
            var buttons = target.childNodes;
            for (var i = 0; i < buttons.length; i++) {
                buttons[i].childNodes[0].innerText == pageNum ? $(buttons[i].childNodes[0]).addClass('current') : null;
            }
        }
    },
    clickNextPrev: function (pageCount, pageObj, random) {
        var _this = this;
        $('.' + random).on('click', '.page a.previewLink', function (e) {
            var kids = e.target.parentNode.parentNode.childNodes;
            var dom = e.target.parentNode.parentNode.parentNode.childNodes[0];
            var tbody = $(e.target.offsetParent).find('tbody')[0];

            for (var i = 0; i < kids.length; i++) {
                if (kids[i].childNodes[0].className.indexOf('current') > -1) {
                    var pageNum = parseInt(kids[i].childNodes[0].text) - 1;
                }
            }
            tbody.innerHTML = _this.render(pageObj.data[pageNum - 1], pageObj.chartConfig, pageObj.drill);
            _this.renderPagination(pageCount, pageNum, pageObj, dom);
            //_this.clickPageNum(pageObj.data, pageObj.chartConfig);
        });
        $('.' + random).on('click', '.page a.nextLink', function (e) {
            var kids = e.target.parentNode.parentNode.childNodes;
            var dom = e.target.parentNode.parentNode.parentNode.childNodes[0];
            var tbody = $(e.target.offsetParent).find('tbody')[0];

            for (var i = 0; i < kids.length; i++) {
                if (kids[i].childNodes[0].className.indexOf('current') > -1) {
                    var pageNum = parseInt(kids[i].childNodes[0].text) + 1;
                }
            }
            tbody.innerHTML = _this.render(pageObj.data[pageNum - 1], pageObj.chartConfig, pageObj.drill);
            _this.renderPagination(pageCount, pageNum, pageObj, dom);
            //_this.clickPageNum(pageObj.data, pageObj.chartConfig);
        });
    },
    export: function (random, data, chartName) {
        // $(".toolbar" + random + " .exportBnt").on('click', function () {
        //     var xhr = new XMLHttpRequest();
        //     var formData = new FormData();
        //     formData.append('data', JSON.stringify({data: data, type: 'table',chartName: chartName}));
        //     xhr.open('POST', 'dashboard/tableToxls.do');
        //     xhr.responseType = 'arraybuffer';
        //     xhr.onload = function (e) {
        //         var blob = new Blob([this.response], {type: "application/vnd.ms-excel"});
        //         var objectUrl = URL.createObjectURL(blob);
        //         var aForExcel = $("<a><span class='forExcel'>下载excel</span></a>").attr("href", objectUrl);
        //         chartName = chartName && chartName.toString().replace("/", "-");
        //         var exportFileName = chartName ? chartName + '.xls' : 'table.xls';
        //         // aForExcel.attr("download", "table.xls");
        //         aForExcel.attr("download", exportFileName);
        //         $("body").append(aForExcel);
        //         $(".forExcel").click();
        //         aForExcel.remove();
        //     };
        //     xhr.send(formData);
        // });

        $(".toolbar" + random + " .exportButton").on('click', function () {
            var xhr = new XMLHttpRequest();
            var formData = new FormData();
            formData.append('data', JSON.stringify({data: data, type: 'table',chartName: chartName}));
            xhr.open('POST', 'dashboard/tableToxls.do');
            xhr.responseType = 'arraybuffer';
            xhr.onload = function (e) {
                var blob = new Blob([this.response], {type: "application/vnd.ms-excel"});
                var objectUrl = URL.createObjectURL(blob);
                var aForExcel = $("<a><span class='forExcel'>下载excel</span></a>").attr("href", objectUrl);
                chartName = chartName && chartName.toString().replace("/", "-");
                var exportFileName = chartName ? chartName + '.xls' : 'table.xls';
                // aForExcel.attr("download", "table.xls");
                aForExcel.attr("download", exportFileName);
                $("body").append(aForExcel);
                $(".forExcel").click();
                aForExcel.remove();
            };
            xhr.send(formData);
        });

    },
    exportByChartId: function (random, data, chartName, chartId) {
        $("#exportBtn" + chartId).on('click', function () {
            var xhr = new XMLHttpRequest();
            var formData = new FormData();
            formData.append('data', JSON.stringify({data: data, type: 'table', chartName: chartName}));
            xhr.open('POST', 'dashboard/tableToxls.do');
            xhr.responseType = 'arraybuffer';
            xhr.onload = function (e) {
                var blob = new Blob([this.response], {type: "application/vnd.ms-excel"});
                var objectUrl = URL.createObjectURL(blob);
                var aForExcel = $("<a><span class='forExcel'>下载excel</span></a>").attr("href", objectUrl);
                chartName = chartName && chartName.toString().replace("/", "-");
                var exportFileName = chartName ? chartName + '.xls' : 'table.xls';
                // aForExcel.attr("download", "table.xls");
                aForExcel.attr("download", exportFileName);
                $("body").append(aForExcel);
                $(".forExcel").click();
                aForExcel.remove();
            };
            xhr.send(formData);
        });
    },
    exportByExportDomElementId: function (exportDomElementId, data, chartName) {
        $("#"+exportDomElementId).on('click', function () {
            var xhr = new XMLHttpRequest();
            var formData = new FormData();
            formData.append('data', JSON.stringify({data: data, type: 'table', chartName: chartName}));
            xhr.open('POST', 'dashboard/tableToxls.do');
            xhr.responseType = 'arraybuffer';
            xhr.onload = function (e) {
                var blob = new Blob([this.response], {type: "application/vnd.ms-excel"});
                var objectUrl = URL.createObjectURL(blob);
                var aForExcel = $("<a><span class='forExcel'>下载excel</span></a>").attr("href", objectUrl);
                chartName = chartName && chartName.toString().replace("/", "-");
                var exportFileName = chartName ? chartName + '.xls' : 'table.xls';
                // aForExcel.attr("download", "table.xls");
                aForExcel.attr("download", exportFileName);
                $("body").append(aForExcel);
                $(".forExcel").click();
                aForExcel.remove();
            };
            xhr.send(formData);
        });
    }
};