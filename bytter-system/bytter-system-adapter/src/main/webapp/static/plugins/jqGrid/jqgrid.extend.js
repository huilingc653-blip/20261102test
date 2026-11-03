/**
 * jqGrid 扩展功能
 *[]
 * 周亮
 * 2017年7月25日
 * 0.0.1 树形表格基础功能实现
 */
layui.define(['jquery','jqGrid','layer'],function(exports){
    var $  = layui.jquery,jQuery=layui.jquery,layer=parent?parent.layer:layui.layer;

    (function (global, factory) {
        "use strict";
        if (typeof define === "function" && define.amd) {
            // AMD. Register as an anonymous module.
            //console.log("grid.base AMD");
            define([
                "jquery"
            ], function ($) {
                //console.log("grid.base AMD: define callback");
                return factory($, global.document);
            });
        } else if (typeof module === "object" && module.exports) {
            // Node/CommonJS
            //console.log("grid.base CommonJS");
            module.exports = function (root, $) {
                //console.log("grid.base CommonJS: in module.exports");
                if (!root) {
                    root = window;
                }
                //console.log("grid.base CommonJS: before require('jquery')");
                if ($ === undefined) {
                    // require("jquery") returns a factory that requires window to
                    // build a jQuery instance, we normalize how we use modules
                    // that require this pattern but the window provided is a noop
                    // if it's defined (how jquery works)
                    $ = typeof window !== "undefined" ?
                        require("jquery") :
                        require("jquery")(root);
                }
                //global.jQuery = $;
                //root.jQuery = $;
                //console.log("grid.base CommonJS: before factory");
                factory($, root.document);
                return $;
            };
        } else {
            // Browser globals
            //console.log("grid.base Browser: before factory");
            factory(jQuery, global.document);
        }
    }(typeof window !== "undefined" ? window : this, function ($, document) {
        "use strict";
        var originalData = [];
        var originalColModel = [];
        var jgrid = $.jgrid,jqID = jgrid.jqID;
        var base = $.fn.jqGrid,mergeCssClasses = jgrid.mergeCssClasses,hasOneFromClasses = jgrid.hasOneFromClasses;
        $.fn.grid = base;
        var getGuiStateStyles = function (path) {
            return base.getGuiStyles.call(this, "states." + path);
        };

        var navSeparatorAdd = base.navSeparatorAdd;
        jgrid.extend({
            expandAll:function(){
                this.each(function () {
                    var $t = this, $self = $($t);
                    var nodes = base.getRootNodes.call($self);
                    base.expandLoop.call($self,nodes);
                });
            },
            expandLoop:function(nodes){
                this.each(function () {
                    var $t = this,p = $t.p,isLeaf = p.treeReader.leaf_field, $self = $($t);
                    for(var i=0;i<nodes.length;i++){
                        var tempNodes = base.getNodeChildren.call($self,nodes[i]);
                        if(tempNodes.length>0){
                            base.expandLoop.call($self,tempNodes);
                        }
                        if(!nodes[i][isLeaf]){
                            base.expandRow.call($self,nodes[i]);
                            base.expandNode.call($self,nodes[i]);
                        }
                    }
                });
            },
            collapseAll:function(){
                this.each(function () {
                    var $t = this, $self = $($t);
                    var nodes = base.getRootNodes.call($self);
                    base.collapseLoop.call($self,nodes);
                });
            },
            collapseLoop:function(nodes){
                this.each(function () {
                    var $t = this,p = $t.p,isLeaf = p.treeReader.leaf_field, $self = $($t);
                    for(var i=0;i<nodes.length;i++){
                        var tempNodes = base.getNodeChildren.call($self,nodes[i]);
                        if(tempNodes.length>0){
                            base.collapseLoop.call($self,tempNodes);
                        }
                        if(!nodes[i][isLeaf]){
                            base.collapseRow.call($self,nodes[i]);
                            base.collapseNode.call($self,nodes[i]);
                        }
                    }
                });
            },
            navGrid: function (elem, oMuligrid, pEdit, pAdd, pDel, pSearch, pView) {
                if (typeof elem === "object") {
                    // the option pager are skipped
                    pView = pSearch;
                    pSearch = pDel;
                    pDel = pAdd;
                    pAdd = pEdit;
                    pEdit = oMuligrid;
                    oMuligrid = elem;
                    elem = undefined;
                }
                pAdd = pAdd || {};
                pEdit = pEdit || {};
                pView = pView || {};
                pDel = pDel || {};
                pSearch = pSearch || {};
                return this.each(function () {
                    var $t = this, p = $t.p, $self = $($t);
                    if (!$t.grid || p == null || ($t.nav && $(elem).find(".navtable").length > 0)) {
                        return; // error or the navigator bar already exists
                    }
                    // make new copy of the options oMuligrid and use it for ONE specific grid.
                    // p.navOptions can contains grid specific options
                    // we will don't modify the input options oMuligrid
                    var gridId = p.id,
                        o = $.extend(
                            {
                                edit: true,
                                add: true,
                                del: true,
                                search: true,
                                refresh: true,
                                refreshstate: "firstpage",
                                view: false,
                                closeOnEscape: true,
                                beforeRefresh: null,
                                afterRefresh: null,
                                cloneToTop: false,
                                hideEmptyPagerParts: true,
                                //jqModal: true,
                                alertwidth: 200,
                                alertheight: "auto",
                                alerttop: null,
                                //alertToTop: false,
                                removemodal: true,
                                alertleft: null,
                                alertzIndex: null,
                                iconsOverText: false
                            },
                            base.getGridRes.call($self, "nav"),
                            jgrid.nav || {},
                            p.navOptions || {},
                            oMuligrid || {}
                        );
                    // set default position depend of RTL/LTR direction of the grid
                    o.position = o.position || (p.direction === "rtl" ? "right" : "left");

                    var twd, tdw, gridIdEscaped = p.idSel, gboxSelector = p.gBox, commonIconClass = o.commonIconClass,
                        alertIDs = { themodal: "alertmod_" + gridId, modalhead: "alerthd_" + gridId, modalcontent: "alertcnt_" + gridId },
                        createModalAlert = function () {
                            return function () {
                                layer.alert(o.alerttext,{icon:2});

                            };
                        },
                        viewModalAlert = createModalAlert(),
                        navtbl,
                        clickOnEnter = function (e) {
                            var $focused;
                            if (e.which === 13) {
                                $focused = $(this).find(".ui-pg-button").filter(":focus");
                                if ($focused.length > 0) {
                                    // $focused[0].id == "view_list" or "view_list_top"
                                    var focusedId = $focused[0].id,
                                        actionName = focusedId.substr(0,
                                            $(this).closest(".ui-jqgrid-toppager").length > 0 ?
                                                focusedId.length - gridId.length - 5 : // "_" + "_top"
                                                focusedId.length - gridId.length - 1), // view "_"
                                        gialogId = actionName + "mod" + p.id, // "viewmodlist"
                                        visibleDailogIds = $(".ui-jqdialog").filter(":visible").map(function () { return this.id; });

                                    if ($.inArray(gialogId, visibleDailogIds) < 0) {
                                        // simulate click only if the dialog is not already opened
                                        $focused.trigger("click");
                                        return false;
                                    }
                                }
                            }
                        },
                        hoverClasses = getGuiStateStyles.call($t, "hover"),
                        disabledClass = getGuiStateStyles.call($t, "disabled"),
                        navButtonClass = base.getGuiStyles.call($t, "navButton", "ui-pg-button");
                    if (!$t.grid) {
                        return; // error
                    }
                    // set modalAlert which can be used inside of
                    $t.modalAlert = viewModalAlert;
                    if (elem === undefined) {
                        if (p.pager) {
                            elem = p.pager;
                            if (p.toppager) {
                                o.cloneToTop = true; // add buttons to both pagers
                            }
                        } else if (p.toppager) {
                            elem = p.toppager;
                        }
                    }

                    var clone = 1, i, tbd, pgid, elemids, iPart, pagerTable, $pagerPart, pagerParts = ["left", "center", "right"],
                        navButtonDisabledClass = base.getGuiStyles.call($t, "navButton", "ui-pg-button" + " " + getGuiStateStyles.call($t, "disabled")),
                        sep = "<div class='" + navButtonDisabledClass + "'><span class='ui-separator'></span></div>",
                        onHoverIn = function () {
                            if (!hasOneFromClasses(this, disabledClass)) {
                                $(this).addClass(hoverClasses);
                            }
                        },
                        onHoverOut = function () {
                            $(this).removeClass(hoverClasses);
                        },
                        onAdd = function () {
                            if (!hasOneFromClasses(this, disabledClass)) {
                                if ($.isFunction(o.addfunc)) {
                                    o.addfunc.call($t, pAdd);
                                } else {
                                    base.editGridRow.call($self, "new", pAdd);
                                }
                            }
                            return false;
                        },
                        editOrViewOfSelectedRow = function (func, methodName, param) {
                            if (!hasOneFromClasses(this, disabledClass)) {
                                var sr = p.selrow;
                                if (sr) {
                                    if ($.isFunction(func)) {
                                        func.call($t, sr, param);
                                    } else {
                                        base[methodName].call($self, sr, param);
                                    }
                                } else {
                                    viewModalAlert();
                                }
                            }
                            return false;
                        },
                        onEdit = function () {
                            return editOrViewOfSelectedRow.call(this, o.editfunc, "editGridRow", pEdit);
                        },
                        onView = function () {
                            return editOrViewOfSelectedRow.call(this, o.viewfunc, "viewGridRow", pView);
                        },
                        onDel = function () {
                            var dr;
                            if (!hasOneFromClasses(this, disabledClass)) {
                                if (p.multiselect) {
                                    dr = p.selarrrow;
                                    if (dr.length === 0) { dr = null; }
                                } else {
                                    dr = p.selrow;
                                }
                                if (dr) {
                                    if ($.isFunction(o.delfunc)) {
                                        o.delfunc.call($t, dr, pDel);
                                    } else {
                                        base.delGridRow.call($self, dr, pDel);
                                    }
                                } else {
                                    viewModalAlert();
                                }
                            }
                            return false;
                        },
                        onSearch = function () {
                            if (!hasOneFromClasses(this, disabledClass)) {
                                if ($.isFunction(o.searchfunc)) {
                                    o.searchfunc.call($t, pSearch);
                                } else {
                                    base.searchGrid.call($self, pSearch);
                                }
                            }
                            return false;
                        },
                        onRefresh = function () {
                            if (!hasOneFromClasses(this, disabledClass)) {
                                if ($.isFunction(o.beforeRefresh)) { o.beforeRefresh.call($t); }
                                p.search = false;
                                p.resetsearch = true;
                                try {
                                    if (o.refreshstate !== "currentfilter") {
                                        p.postData.filters = "";
                                        try {
                                            $("#fbox_" + gridIdEscaped).jqFilter("resetFilter");
                                        } catch (ignore) { }
                                        if ($.isFunction($t.clearToolbar)) { $t.clearToolbar(false); }
                                    }
                                } catch (ignore) { }
                                switch (o.refreshstate) {
                                    case "firstpage":
                                        $self.trigger("reloadGrid", [$.extend({}, o.reloadGridOptions || {}, { page: 1 })]);
                                        break;
                                    case "current":
                                    case "currentfilter":
                                        $self.trigger("reloadGrid", [$.extend({}, o.reloadGridOptions || {}, { current: true })]);
                                        break;
                                }
                                if ($.isFunction(o.afterRefresh)) { o.afterRefresh.call($t); }
                            }
                            return false;
                        },
                        stdButtonActivation = function (name, id, onClick) {
                            var $button = $("<div class='" + navButtonClass + "' tabindex='0' role='button'></div>"),
                                iconClass = o[name + "icon"],
                                iconText = $.trim(o[name + "text"]);
                            $button.append("<div class='ui-pg-div'><span class='" +
                                (o.iconsOverText ?
                                    mergeCssClasses("ui-pg-button-icon-over-text", commonIconClass, iconClass) :
                                    mergeCssClasses(commonIconClass, iconClass)) +
                                "'></span>" +
                                (iconText ? "<span class='ui-pg-button-text" + (o.iconsOverText ? " ui-pg-button-icon-over-text" : "") + "'>" + iconText + "</span>" : "") +
                                "</div>");
                            $(navtbl).append($button);
                            $button.attr({ "title": o[name + "title"] || "", id: id || name + "_" + elemids })
                                .click(onClick)
                                .hover(onHoverIn, onHoverOut);
                            return $button;
                        };

                    if (o.cloneToTop && p.toppager) { clone = 2; }
                    for (i = 0; i < clone; i++) {
                        // we can set aria-activedescendant="idOfFirstButton" later
                        navtbl = $("<div" + " class='ui-pg-table navtable' role='toolbar' style='float:" +
                            (p.direction === "rtl" ? "right" : "left") +
                            ";table-layout:auto;'></div>");
                        if (i === 0) {
                            pgid = elem;
                            elemids = gridId;
                            if (pgid === p.toppager) {
                                elemids += "_top";
                                clone = 1;
                            }
                        } else {
                            pgid = p.toppager;
                            elemids = gridId + "_top";
                        }
                        if (o.add) {
                            stdButtonActivation("add", pAdd.id, onAdd);
                        }
                        if (o.edit) {
                            stdButtonActivation("edit", pEdit.id, onEdit);
                        }
                        if (o.view) {
                            stdButtonActivation("view", pView.id, onView);
                        }
                        if (o.del) {
                            stdButtonActivation("del", pDel.id, onDel);
                        }
                        if (o.add || o.edit || o.del || o.view) { $(navtbl).append(sep); }
                        if (o.search) {
                            tbd = stdButtonActivation("search", pSearch.id, onSearch);
                            if (pSearch.showOnLoad && pSearch.showOnLoad === true) {
                                $(tbd, navtbl).click();
                            }
                        }
                        if (o.refresh) {
                            stdButtonActivation("refresh", "", onRefresh);
                        }
                        // TODO use setWidthOfPagerTdWithPager or remove at all and use div structure with wrapping
                        tdw = $(".ui-jqgrid>.ui-jqgrid-view").css("font-size") || "11px";
                        $("body").append("<div id='testpg2' class='" + base.getGuiStyles.call($t, "gBox", "ui-jqgrid") + "' style='font-size:" + tdw + ";visibility:hidden;' ></div>");
                        twd = $(navtbl).clone().appendTo("#testpg2").width();
                        $("#testpg2").remove();
                        $(pgid + "_" + o.position, pgid).append(navtbl);
                        if (o.hideEmptyPagerParts) {
                            for (iPart = 0; iPart < pagerParts.length; iPart++) {
                                if (pagerParts[iPart] !== o.position) {
                                    $pagerPart = $(pgid + "_" + pagerParts[iPart], pgid);
                                    if ($pagerPart.length === 0 || $pagerPart[0].childNodes.length === 0) {
                                        $pagerPart.hide();
                                    } else if ($pagerPart[0].childNodes.length === 1) {
                                        pagerTable = $pagerPart[0].firstChild;
                                        if ($(pagerTable).is("table.ui-pg-table") && (pagerTable.rows === 0 || pagerTable.rows[0].cells.length === 0)) {
                                            $pagerPart.hide();
                                        }
                                    }
                                }
                            }
                        }
                        if (p._nvtd) {
                            if (twd > p._nvtd[0]) {
                                $(pgid + "_" + o.position, pgid).width(twd);
                                p._nvtd[0] = twd;
                            }
                            p._nvtd[1] = twd;
                        }
                        $t.nav = true;
                        navtbl.on("keydown.jqGrid", clickOnEnter);
                    }
                    $self.triggerHandler("jqGridResetFrozenHeights");
                });
            },
            getColModel:function(){
                var colModel;
                this.each(function () {
                    var $t = this,p = $t.p;
                    colModel = p.colModel;
                });
                return colModel;
            },
            /**
             * 添加自定义按钮，包括头部
             * @param options
             */
            customButton:function(options){
                return this.each(function () {
                    var $t = this, $self = $($t);
                    if(!options.caption){
                        options.caption = "";
                    }
                    base.navButtonAdd.call($self,'#'+$self[0].id+"_jqGridPager",options);
                    base.navButtonAdd.call($self,'#'+$self[0].id+"_toppager",options);
                });
            },
            /**
             * 添加按钮的分隔符
             */
            navSeparatorAdd:function(){
                var $t = this, $self = $($t);
                navSeparatorAdd.call($self,'#'+$self[0].id+"_jqGridPager",{});
                navSeparatorAdd.call($self,'#'+$self[0].id+"_toppager",{});
            },
            /**
             * 设置真实数据
             * @param data
             */
            setOriginalData:function(data, pk){
                originalData = [];
                if(!pk){
                    pk = 'id';
                }
                if(data.rows){
                    for(var i=0;i<data.rows.length;i++){
                        var temp = data.rows[i];
                        originalData[temp[pk]] = temp;
                    }
                }
            },
            /**
             * 获取真实数据
             * @param rowId
             * @returns {*}
             */
            getOriginalData:function(rowId){
                return originalData[rowId]
            },
            /**
             * 获取选中行的ID
             * @returns {*}
             */
            getSelectRowsId:function(){
                var $t = this, $self = $($t);
                var rowIds = base.getGridParam.call($self,"selarrrow");
                return rowIds;
            },
            /**
             * 获取选中行的数据
             * @returns {Array}
             */
            getSelectRowsData:function(){
                var $t = this, $self = $($t);
                var temp = [];
                var ids = base.getSelectRowsId.call($self);
                for(var i=0;i<ids.length;i++){
                    var id = ids[i];
                    var data = base.getOriginalData.call($self,id);
                    if(data){
                        temp.push(data);
                    }
                }
                return temp;
            }
        });


        $.extend(true, jgrid, {icons:{fontAwesome:{treeGrid:{leaf:"",minus:"fa-lg  fa-chevron-down",plusLtr: "fa-lg fa-chevron-right"}}}});
    }));

    //输出test接口
    exports('gridExtend', {});
});