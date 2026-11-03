'use strict';
avalon.component('notification', {
    template: heredoc(function () {
        /*
         <div class="notification layui-anim layui-anim-upbit" :css="{display:@show?'block':'none',right:@offsetX}">
             <div class="notification-header">
                <span class="notification-close-all" title="所有标记已读" :if="@notificationType==1" :click="@deleteNotification()" ><i class="fa fa-trash-o" aria-hidden="true"></i></span>
                 <span class="notification-close" :click="@closeNotification"><i class="fa fa-times" aria-hidden="true"></i></span>
                 <div class="layui-btn-group">
                 <button class="layui-btn layui-btn-primary layui-btn-sm" :class="[(@notificationType==1?'active':'')]" :click="@toggleNotificationType(1)">通知</button>
                 <button class="layui-btn layui-btn-primary layui-btn-sm" :class="[(@notificationType==2?'active':'')]" :click="@toggleNotificationType(2)">待处理</button>
                 </div>
             </div>
             <div class="notification-body">
                 <ul class="content">
                     <li :for="(index,notification) in @notificationData" :click="@toggleContent(index,@notification)">
                         <div class="content-header">
                             <div class="category">{{notification.categoryName}}</div>
                             <div class="time">{{notification.createDate}}</div>
                         </div>
                         <div class="content-detail" :class="[(@notification.expand?'expand':'')]">{{notification.name}}</div>
                         <span class="delete-icon" title="标记已读" :if="@notificationType==1" :click="@deleteNotification(index)"><i class="fa fa-trash-o" aria-hidden="true"></i></span>
                         <span class="delete-icon" title="前往处理" :if="@notificationType==2" :click="@jumpListPage(notification)"><i class="fa fa-arrow-circle-right" aria-hidden="true"></i></span>
                     </li>
                 </ul>
                 <div class="more" :if="!@loading && @totalPage>0 && @page!=@totalPage">
                    <a href="javascript:void(0)" :click="@getData">加载更多</a>
                 </div>
                 <div class="loading" :if="@loading"><p class="loading-text">载入中...</p></div>
                 <div class="empty" :if="!@loading && @emptyContent">暂无内容</div>
             </div>
         </div>
         */
    }),
    defaults: {
        show:false,
        loading:false,
        emptyContent:false,
        reload:false,
        notificationType:1,
        element:"",
        shakeDot:{},
        notificationData:[],
        offsetX:10,
        page:0,
        totalPage:0,
        notificationNumber:0,
        notificationNumberRead:false,
        onReady:function(e){
            var that = this;
            if(that.element){
                var elem = $("."+this.element);
                var elemWidth = elem.outerWidth() / 2;
                var offsetRight = $(document).width() - elem.offset().left - elem.outerWidth();
                var right = elemWidth + offsetRight - 175;
                that.offsetX = right>0?right:0;
                elem.off().click(function(){
                    that.show = !that.show;
                    that.notificationNumberRead = true;
                    if(that.notificationData.length===0){
                        that.getData();
                    }
                });
            }
            try{$(".notification .notification-body").mCustomScrollbar({theme:"minimal-dark"});}catch(e){};

            that.shakeDot = $("."+that.shakeDot);

            that.checkNotificationNumber();

            $('.notification').bind("mousedown", function( event ) {
                event.preventDefault();
            }).hover(function(){
            },function(){
                that.closeNotification();
            });

            var intervalTime = 1000 * 60 * 5;
            setInterval(function(){
                that.checkNotificationNumber();
            },intervalTime);

        },
        checkNotificationNumber:function(){
            var that = this;
            $.get(basePath+'/sys/notification/count',function(result){
                if(result){
                    if(that.notificationNumber !== result){
                        that.notificationNumberRead = false;
                    }
                    that.notificationNumber = result;
                }else{
                    that.shakeDot.animate({ opacity: "0"});
                }
                if(!that.notificationNumberRead && that.notificationNumber > 0) {
                    that.shake(that.shakeDot);
                }
            },'JSON');
        },
        shake:function(jqNode){
            var that = this;
            if(that.notificationNumber > 0){
                if(that.notificationNumberRead){
                    that.shakeDot.animate({ opacity: "1"});
                }else{
                    jqNode.animate({ opacity: "1"},500).animate({ opacity: "0"},200,function(){
                        that.shake(jqNode);
                    });
                }
            }else{
                that.shakeDot.animate({ opacity: "0"});
            }
        },
        closeNotification:function(){
            this.show = false;
        },
        toggleNotificationType:function(type){
            this.notificationType = type;
            this.reload = true;
            this.page = 0;
            this.getData();
        },
        deleteNotification:function(index){
            var that = this;
            var ids = [];
            var nd = that.notificationData.$model;
            if(index !== undefined){
                ids.push(nd[index].id);
            }else{
                for(var i=0;i<nd.length;i++){
                    ids.push(nd[i].id);
                }
            }
            if(ids.length === 0){
                return;
            }
            $.post(basePath + "/sys/notification/delete", {ids: ids}, function (result) {
                if(result.success){
                    if(index !== undefined){
                        that.notificationData.removeAt(index);
                    }else{
                        that.notificationData.removeAll();
                        that.page = 0;
                    }
                }
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        },
        jumpListPage:function(notification){
            if(bt && bt.navtab && notification.listUrl){
                bt.navtab.addNavtab("待处理 - " + notification.categoryName, basePath + notification.listUrl);
                this.closeNotification();
            }
        },
        toggleContent:function(index,notification){
            var temps = this.notificationData.$model;
            temps[index].expand = true;
            this.notificationData.removeAll();
            this.notificationData = temps;
        },
        getData:function(){
            var that = this;
            if(that.loading){
                return;
            }
            if(that.reload){
                that.notificationData.removeAll();
            }
            that.loading = true;
            that.reload = false;
            var tempData = {
                pageNumber:++that.page,
                type:that.notificationType
            };
            $.ajax({
                type: "POST",
                url: basePath + "/sys/notification/list",
                data: JSON.stringify(tempData),//将对象序列化成JSON字符串
                dataType:"json",
                contentType : 'application/json;charset=utf-8', //设置请求头信息
                success: function(result){
                    setTimeout(function(){
                        that.page = result.pageNumber;
                        that.totalPage = result.totalPage;
                        that.emptyContent = !result.data.length>0;
                        if(result.data.length>0){
                            for(var i=0;i<result.data.length;i++){
                                result.data[i].expand = false;
                                if(!result.data[i].name){
                                    result.data[i].name = " ";
                                }
                                if(!result.data[i].createDate){
                                    result.data[i].createDate = " ";
                                }else{
                                    //liuyj 将时间戳转为时间
                                    var time = result.data[i].createDate;
                                    var date = new Date(time);
                                    var Y = date.getFullYear() + '-';
                                    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
                                    var D = ( date.getDate() < 10 ? ( '0'+ date.getDate() ):date.getDate() ) + ' ';
                                    var h = ( date.getHours() < 10 ? ( '0'+ date.getHours() ):date.getHours() ) + ':';
                                    var m = ( date.getMinutes() < 10 ? ( '0'+ date.getMinutes() ):date.getMinutes() ) + ':';
                                    var s = date.getSeconds() < 10 ? ( '0'+ date.getSeconds() ):date.getSeconds() ;
                                    result.data[i].createDate = Y+M+D+h+m+s;
                                }
                            }
                            that.notificationData.pushArray(result.data);
                        }
                        that.loading = false;
                    },500)
                }
            });

        }

    }
});