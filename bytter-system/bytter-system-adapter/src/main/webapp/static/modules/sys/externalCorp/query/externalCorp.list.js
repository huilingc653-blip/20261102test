bt.use(['datatable', 'jquery', 'corp', 'bankInfo','btImport','element','dicts','mulselect','selectType'], function () {
    var layer = parent ? parent.bt.layer : bt.layer;
    var element = bt.element();
    var dataform = bt.dataform;
    bt.dicts.init();
    var $ = bt.jquery,form = bt.form();
    var corp = bt.corp.init();
    bt.mulselect.init();
    bt.bankInfo.init();
    bt.selectType.init();

    /*element.on('tab(externalCorpTabBrief)', function(data){
        status = this.getAttribute('data-status');
        $("input[name='isCommon']").val(status);
        showOrHiddenCorp($("input[name='isCommon']").val());
        table.draw();
    });*/

    function escapeBelongCorpHtml(text) {
        return String(text)
            .replace(/&/g, '&amp;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#39;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;');
    }

    function formatBelongCorp(full) {
        if (!full || !full.corpName) {
            return "";
        }
        var names = String(full.corpName).split(',');
        var codes = full.corpCode ? String(full.corpCode).split(',') : [];
        var parts = [];
        for (var i = 0; i < names.length; i++) {
            var name = $.trim(names[i]);
            if (!name) {
                continue;
            }
            var code = codes[i] ? $.trim(codes[i]) : '';
            parts.push(code ? ('(' + code + ')' + name) : name);
        }
        return parts.join(',');
    }

    function renderBelongCorp(data, type, full) {
        var text = formatBelongCorp(full);
        if (!text) {
            return "";
        }
        if (type !== 'display') {
            return text;
        }
        // 完整内容进单元格，省略号随列宽自适应；拉宽列即可看到更多，不再写死 max-width
        return '<div class="ellipsis" title="' + escapeBelongCorpHtml(text) + '">'
            + escapeBelongCorpHtml(text) + '</div>';
    }

    var table = bt.datatable.init({
        elem: "#externalCorpTable",
        ignoreButtons:['colvis','output','print'],
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1,'asc']],
        searchForm: {
        	formElem: "#searchForm",
        	formFilter: "search"
        },
        ajax: {
            'url': basePath + "/sys/externalCorp/datatable.json",
        },
        buttons: [],
        columns: [
            {data: "code",bSortable:true},
            {data: "name",bSortable:true},
            {data: "abbreviate", orderable: false},
            {data: "nameEn", orderable: false},
            {data: "isNative", orderable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.isNative);
                }
            },
            {data: "corpName", orderable: false, render: renderBelongCorp},
            {data: "sourceSystem",bSortable:true},
            {
                data: "status",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("bankAcc.validSign", full.status);
                }
            },
            {
                data: "externalType",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("external.type", full.externalType);
                }
            },
            {data: "supplierName",bSortable:true,
                render:function (data,type,full,meta) {
                    if (full.externalType == 1 || full.externalType == 2){
                        return full.supplierName;
                    }else{
                        return "";
                    }
                }
            },
            {data: "bwTypeName",bSortable:true},
            {data: "socCode",bSortable:true},
            {data: "proName",bSortable:true},
            {data: "auditStatus",bSortable:true,
                render:function (data,type,full,meta) {
                    return bt.dict("externalCorp.auditStatus", full.auditStatus);
                }
            },
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '&nbsp;<a href="javascript:void(0);" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
    });

    //showOrHiddenCorp(1);

    view = function (el) {
        var data = layui.jquery("#externalCorpTable").DataTable().row(layui.jquery(el).data("row")).data();
        var url = basePath + "/sys/externalCorp/view?id=" + data.id + "&corpId=" + data.corpId;
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.view.data'), url);

        }
    }

    function showOrHiddenCorp(value) {
        /*Tab为共享单位时,隐藏单位列*/
        var column = table.column(5);
        if (value == 1){
            column.visible(false);
            $(".inlineCorp").attr("style", "display:none");
        }else{
            column.visible(true);
            $(".inlineCorp").attr("style", "display:inlineblock");
        }
    }
});
