/** layui-v1.0.9_rls MIT License By http://www.layui.com */
;
bt.define(function(a) {
    "use strict";
    function t(a) {
        new p(a)
    }
    var e = document,
        r = "getElementById",
        n = "getElementsByTagName",
        s = 0,
        p = function(a) {
            var t = this,
                e = t.config = a || {};
            e.item = s++,
                t.render(!0)
        };
    p.on = function(a, t, e) {
        return a.attachEvent ? a.attachEvent("on" + t,
            function() {
                e.call(a, window.even)
            }) : a.addEventListener(t, e, !1),
            p
    },
        p.prototype.type = function() {
            var a = this.config;
            if ("object" == typeof a.cont) return void 0 === a.cont.length ? 2 : 3
        },
        p.prototype.view = function() {
            var a = this,
                t = a.config,
                e = [],
                r = {};

             if(t.hasOwnProperty('limits') && t.hasOwnProperty('limit')){
                 var html = '<span class="layui-laypage-limits">每 页<select>';
                 var selected = "";
                 for (var i = 0; i < t.limits.length; i++) {
                     if (t.limits[i] == t.limit) {
                         selected = ' selected="selected"';
                     } else {
                         selected = "";
                     }
                     html +=
                         '<option value="' +
                         t.limits[i] +
                         '"' +
                         selected +
                         ">" +
                         t.limits[i] +
                         "</option>";
                 }

                 html+='</select>项 <div class="lay-laypage-text">'+"当前显示第 "+((t.curr- 1) * t.limit + 1)+"  至  "+((t.curr == t.pages)? t.resultCount : (parseInt(t.limit)> parseInt(t.resultCount)? t.resultCount : t.curr*t.limit))+" 项，全部  "+t.resultCount+"  项。"+"</div></span>"
                 e.push(html)
             }
            if (t.pages = 0 | t.pages, t.curr = 0 | t.curr || 1, t.groups = "groups" in t ? 0 | t.groups: 5, t.first = "first" in t ? t.first:bt.lang('common.table.firstPage'), t.last = "last" in t ? t.last: bt.lang('common.table.lastPage'), t.prev = "prev" in t ? t.prev: bt.lang('common.table.previousPage'), t.next = "next" in t ? t.next:bt.lang("common.table.nextPage"), t.pages < 1) return "";
            for (t.groups > t.pages && (t.groups = t.pages), r.index = Math.ceil((t.curr + (t.groups > 1 && t.groups !== t.pages ? 1 : 0)) / (0 === t.groups ? 1 : t.groups)),  e.push('<a href="javascript:;" class="layui-laypage-prev " data-page="' + (t.curr - 1) + '"><div class="drawl"></div></a>'), r.index > 1 && t.first && 0 !== t.groups && e.push("<span>&#x2026;</span>"), r.poor = Math.floor((t.groups - 1) / 2), r.start = r.index > 1 ? t.curr - r.poor: 1, r.end = r.index > 1 ?
                function() {
                    var a = t.curr + (t.groups - r.poor - 1);
                    return a > t.pages ? t.pages: a
                } () : t.groups, r.end - r.start < t.groups - 1 && (r.start = r.end - t.groups + 1); r.start <= r.end; r.start++) r.start === t.curr ? e.push('<span class="layui-laypage-curr"><em class="layui-laypage-em" ' + (/^#/.test(t.skin) ? 'style="background-color:' + t.skin + ';"': "") + "></em><em>" + r.start + "</em></span>") : e.push('<a href="javascript:;" data-page="' + r.start + '">' + r.start + "</a>");
            return t.pages > t.groups && r.end < t.pages && t.last && 0 !== t.groups && (e.push('<span>&#x2026;</span><a href="javascript:;" data-page="' + t.pages + '">'+t.pages+'</a>')),
                r.flow = !t.prev && 0 === t.groups,
            (t.curr !== t.pages && t.next || r.flow) && e.push(function() {
                return r.flow && t.curr === t.pages ? '<span class="layui-laypage-nomore" title="&#x5DF2;&#x6CA1;&#x6709;&#x66F4;&#x591A;">' + t.next + "</span>": '<a href="javascript:;" class="layui-laypage-next" data-page="' + (t.curr + 1) + '"><div class="drawr"></div></a>'
            } ()),
            (t.curr == t.pages && t.next || r.flow) && e.push(function() {
                return r.flow && t.curr === t.pages ? '<span class="layui-laypage-nomore" title="&#x5DF2;&#x6CA1;&#x6709;&#x66F4;&#x591A;">' + t.next + "</span>": '<a class="layui-laypage-next" data-page="' + (t.curr) + '" style="pointer-events: none;cursor: default;opacity: 0.5;"><div class="drawr"></div></a>'
            } ()),
            '<div class="relaypage layui-box layui-laypage layui-laypage-' + (t.skin ?
                function(a) {
                    return /^#/.test(a) ? "molv": a
                } (t.skin) : "default") + '" id="layui-laypage-' + a.config.item + '">' + e.join("") +
            function() {
                if(window.top && window.top.enterWithRelaypage){
                    // 解决重载的问题
                    if(!a.refreshFlag){
                        window.top.enterWithRelaypage = false;
                    }
                    return t.skip ? '<span class="layui-laypage-total">'+bt.lang("跳至")+'<input type="number" min="1"  onkeyup="this.value=this.value.replace(/\\D/, \'\');" value="' + t.curr + '" class="layui-laypage-skip">'+bt.lang("common.table.page")+'': ""
                }
                return t.skip ? '<span class="layui-laypage-total">'+bt.lang("跳至")+'<input type="number" min="1"  onkeyup="this.value=this.value.replace(/\\D/, \'\');" class="layui-laypage-skip">'+bt.lang("common.table.page")+'': ""
            } () +"</div>"

        },


        p.prototype.jump = function(a) {
            if (a) {
                for (var t = this,
                         e = t.config,
                         r = a.children,
                         f = a[n]("select")[0],
                         s = a[n]("button")[0],
                         i = a[n]("input")[0],
                         u = 0,
                         o = r.length;
                     u < o; u++)
                    "a" === r[u].nodeName.toLowerCase() && p.on(r[u],
                    "click",
                    function() {
                        var a = 0 | this.getAttribute("data-page");
                        e.curr = a,
                            t.render()
                    });


                i &&
                p.on(i, "keydown", function (even) {
                    var value=  i.value
                    if(even.keyCode==13 && value <= e.pages){
                        // 标识点击回车
                        window.top.enterWithRelaypage = true;
                        t.refreshFlag = true;
                        e.curr = value,
                            t.render();
                    }
                });


                f &&
                p.on(f, "change", function () {
                    // 下拉选择每页条数事件
                    var ev = parseInt(this.value);
                    e.curr * ev > e.resultCount && (e.curr = Math.ceil(e.resultCount / ev)),
                        e.limit = ev
                        t.render();
                });
                s && p.on(s, "click",
                    function() {
                        var a = 0 | i.value.replace(/\s|\D/g, "");
                        a && a <= e.pages && (e.curr = a, t.render())
                    })
            }
        },
        p.prototype.render = function(a) {
            var t = this,
                n = t.config,
                s = t.type(),
                p = t.view();
            2 === s ? n.cont.innerHTML = p: 3 === s ? n.cont.html(p) : e[r](n.cont).innerHTML = p,
            n.jump && n.jump(n, a),
                t.jump(e[r]("layui-laypage-" + n.item)),
            n.hash && !a && (location.hash = "!" + n.hash + "=" + n.curr)
        },
        a("relaypage", t)
}).addcss('../../../css/relaypage.css', 'relaypagecss');
