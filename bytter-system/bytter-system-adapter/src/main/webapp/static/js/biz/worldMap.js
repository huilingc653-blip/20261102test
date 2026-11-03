/**
 * 银行帐号格式化组件
 * 本组件依赖echart / world / china
 * liuyj
 * 2020年02月24日
 * 0.0.1 世界地图

 */
layui.define(['jquery','form','datatablecore','date','element'], function(exports){
    var $ = layui.jquery,form = layui.form(),element = layui.element();;
    var Map =function(){
        this.config={
            clickChinaMapCallback:undefined,
        };
        this.myChart = null;
        this.myProvinceChart = null;
        this.myPieChart = null;
        this.table = null;
        this.isAjaxing = false;
        this.option = {
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    var html = '<div class="area-title">' + params.name + '</div>', data = params.data.data;
                    if (data && data.length) {
                        for (var i = 0; i < data.length; i++) {
                            html = html + '<div class="layui-clear"><span class="area-name">' + data[i].name + '</span><span class="area-value">' + data[i].value + '</span></div>';
                        }
                    }
                    return html
                }
            },
            visualMap: {
                min: 1,
                max: 10,
                // min: 500000,
                // max: 38000000,
                text: ['High', 'Low'],
                realtime: false,
                calculable: true,
                // categories: [ '北美洲','南美洲','欧洲','亚洲其他地区','中国','东南亚及日韩','非洲','大洋洲'],
                // inRange: {
                //     color: ['#B9C594', '#E7C772','#D2D2E7','#B3D6D1','#E38B78','#E2D5BC','#AAC8ED','#9DB6BA'],
                // },
                // color: ['#E38B78','yellow','lightskyblue']
            },
            series: [
                {
                    name: '资金地图',
                    type: 'map',
                    mapType: 'world',
                    label: {
                        normal: {
                            show: false, //显示省份标签
                            textStyle: {
                                color: "#fff"
                            } //省份标签字体颜色
                        },
                        emphasis: { //对应的鼠标悬浮效果
                            show: false,
                            textStyle: {
                                color: "#fff"
                            }
                        }
                    },
                    aspectScale: 0.75,
                    zoom: 1.2,
                    itemStyle: {
                        normal: {
                            borderWidth: 1.5, //区域边框宽度
                            borderColor: '#fff', //区域边框颜色
                            areaColor: "#fff", //区域颜色
                        },
                        emphasis: {
                            borderWidth: 1.5,
                            borderColor: '#fff',
                            areaColor: "rgba(61,142,255,1)",
                        }
                    },
                    roam: true,//是否开启鼠标缩放和平移漫游
                    nameMap: {
                        selectedMode: 'multiple',
                        'China': '中国',
                        'Japan': '东南亚及日韩',
                        'Korea': '东南亚及日韩',
                        'Brunei': '东南亚及日韩',
                        'Timor-Leste': '东南亚及日韩',
                        'Dem. Rep. Korea': '东南亚及日韩',
                        'Malaysia': '东南亚及日韩',
                        'Myanmar': '东南亚及日韩',
                        'Vietnam': '东南亚及日韩',
                        'Thailand': '东南亚及日韩',
                        'Cambodia': '东南亚及日韩',
                        'Philippines': '东南亚及日韩',
                        'Lao PDR': '东南亚及日韩',
                        'Indonesia': '东南亚及日韩',
                        'India': '亚洲其他地区',
                        'Bhutan': '亚洲其他地区',
                        'Nepal': '亚洲其他地区',
                        'Bangladesh': '亚洲其他地区',
                        'Sri Lanka': '亚洲其他地区',
                        'Pakistan': '亚洲其他地区',
                        'Afghanistan': '亚洲其他地区',
                        'Iran': '亚洲其他地区',
                        'Turkmenistan': '亚洲其他地区',
                        'Tajikistan': '亚洲其他地区',
                        'Kyrgyzstan': '亚洲其他地区',
                        'Kazakhstan': '亚洲其他地区',
                        'Mongolia': '亚洲其他地区',
                        'Uzbekistan': '亚洲其他地区',
                        'United Arab Emirates': '亚洲其他地区',
                        'Oman': '亚洲其他地区',
                        'Yemen': '亚洲其他地区',
                        'Saudi Arabia': '亚洲其他地区',
                        'Jordan': '亚洲其他地区',
                        'Iraq': '亚洲其他地区',
                        'Syria': '亚洲其他地区',
                        'Qatar': '亚洲其他地区',
                        'Azerbaijan': '亚洲其他地区',
                        'Turkey': '亚洲其他地区',
                        'Armenia': '亚洲其他地区',
                        'Georgia': '亚洲其他地区',
                        '': '亚洲其他地区',
                        'Cyprus': '亚洲其他地区',

                        'Russia': '欧洲',
                        'Finland': '欧洲',
                        'Estonia': '欧洲',
                        'Latvia': '欧洲',
                        'Lithuania': '欧洲',
                        'Ukraine': '欧洲',
                        'Belarus': '欧洲',
                        'Moldova': '欧洲',
                        'Bulgaria': '欧洲',
                        'Romania': '欧洲',
                        'Greece': '欧洲',
                        'Macedonia': '欧洲',
                        'Albania': '欧洲',
                        'Montenegro': '欧洲',
                        'Serbia': '欧洲',
                        'Bosnia and Herz.': '欧洲',
                        'Croatia': '欧洲',
                        'Sweden': '欧洲',
                        'Norway': '欧洲',
                        'Denmark': '欧洲',
                        'Poland': '欧洲',
                        'Slovakia': '欧洲',
                        'Hungary': '欧洲',
                        'Czech Rep.': '欧洲',
                        'Austria': '欧洲',
                        'Slovenia': '欧洲',
                        'Italy': '欧洲',
                        'Switzerland': '欧洲',
                        'Germany': '欧洲',
                        'Netherlands': '欧洲',
                        'Belgium': '欧洲',
                        'France': '欧洲',
                        'Spain': '欧洲',
                        'Portugal': '欧洲',
                        'United Kingdom': '欧洲',
                        'Ireland': '欧洲',
                        'Iceland': '欧洲',
                        'Australia': '大洋洲',
                        'Papua New Guinea': '大洋洲',
                        'New Zealand': '大洋洲',
                        'New Caledonia': '大洋洲',
                        'Solomon Islands': '大洋洲',
                        'Fiji': '大洋洲',
                        'Vanuatu': '大洋洲',
                        'United States': '北美洲',
                        'Canada': '北美洲',
                        'Greenland': '北美洲',
                        'Guatemala': '北美洲',
                        'Belize': '北美洲',
                        'Honduras': '北美洲',
                        'El Salvador': '北美洲',
                        'Panama': '北美洲',
                        'Mexico': '北美洲',
                        'Cuba': '北美洲',
                        'Nicaragua': '北美洲',
                        'Costa Rica': '北美洲',
                        'Jamaica': '北美洲',
                        'Haiti': '北美洲',
                        'Bahamas': '北美洲',
                        'Dominican Rep.': '北美洲',
                        'Puerto Rico': '北美洲',

                        'Egypt': '非洲',
                        'Libya': '非洲',
                        'Tunisia': '非洲',
                        'Algeria': '非洲',
                        'Morocco': '非洲',
                        'Sudan': '非洲',
                        'Eritrea': '非洲',
                        'Somalia': '非洲',
                        'Ethiopia': '非洲',
                        'S. Sudan': '非洲',
                        'Chad': '非洲',
                        'Niger': '非洲',
                        'Mali': '非洲',
                        'W. Sahara': '非洲',
                        'Mauritania': '非洲',
                        'Senegal': '非洲',
                        'Guinea': '非洲',
                        'Guinea Bissau': '非洲',
                        'Burkina Faso': '非洲',
                        'Ghana': '非洲',
                        'Togo': '非洲',
                        'Benin': '非洲',
                        'Liberia': '非洲',
                        "CÃ´te d'Ivoire": '非洲',
                        'Sierra Leone': '非洲',
                        'Eq Guinea': '非洲',
                        'Central African Rep.': '非洲',
                        'Congo': '非洲',
                        'Gabon': '非洲',
                        'Dem. Rep. Congo': '非洲',
                        'Uganda': '非洲',
                        'Tanzania': '非洲',
                        'Rwanda': '非洲',
                        'Burundi': '非洲',
                        'Kenya': '非洲',
                        'Angola': '非洲',
                        'Namibia': '非洲',
                        'South Africa': '非洲',
                        'Cameroon': '非洲',
                        'Botswana': '非洲',
                        'Zambia': '非洲',
                        'Zimbabwe': '非洲',
                        'Malawi': '非洲',
                        'Mozambique': '非洲',
                        'Lesotho': '非洲',
                        'Nigeria': '非洲',
                        'Guinea-Bissau': '非洲',
                        'Swaziland': '非洲',
                        'Madagascar': '非洲',

                        'Colombia': '南美洲',
                        'Venezuela': '南美洲',
                        'Guyana': '南美洲',
                        'Suriname': '南美洲',
                        'Brazil': '南美洲',
                        'Ecuador': '南美洲',
                        'Peru': '南美洲',
                        'Bolivia': '南美洲',
                        'Chile': '南美洲',
                        'Argentina': '南美洲',
                        'Uruguay': '南美洲',
                        'Paraguay': '南美洲',
                        'French Guiana': '南美洲',

                    },
                }
            ],
            dataRange: {
                splitList: [
                    {start: 1, end: 1, label: '北美洲', color: '#B9C594'},
                    {start: 2, end: 2, label: '南美洲', color: '#E7C772'},
                    {start: 3, end: 3, label: '欧洲', color: '#D2D2E7'},
                    {start: 4, end: 4, label: '亚洲其他地区', color: '#B3D6D1'},
                    {start: 5, end: 5, label: '中国', color: '#E38B78'},
                    {start: 6, end: 6, label: '东南亚及日韩', color: '#E2D5BC'},
                    {start: 7, end: 7, label: '非洲', color: '#AAC8ED'},
                    {start: 8, end: 8, label: '大洋洲', color: '#9DB6BA'},
                ]
            }
        };
        //中国地图参数
        this.chinaOption = {
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    //  debugger;
                    var html = '<div class="area-title">' + params.name + '</div>', data = params.data.data;

                    if (data && data.length) {
                        for (var i = 0; i < data.length; i++) {
                            html = html + '<div class="layui-clear"><span class="area-name">' + data[i].name + '</span><span class="area-value">' + data[i].value + '</span></div>';
                        }
                    }
                    return html
                }
            },
            series: [{
                type: 'map',
                mapType: 'china',
                id:'chinaMap',
                label: {
                    normal: {
                        show: true, //显示省份标签
                        textStyle: {
                            color: "#333",
                            fontSize:"14px",
                            fontWeight:400,
                            fontFamily:"PingFangSC-Regular,PingFang SC"

                        } //省份标签字体颜色
                    },
                    emphasis: { //对应的鼠标悬浮效果
                        show: false,
                        textStyle: {
                            color: "#333"
                        }
                    }
                },
                aspectScale: 0.75,//这个参数用于 scale 地图的长宽比。最终的 aspect 的计算方式是：geoBoundingRect.width / geoBoundingRect.height * aspectScale
                zoom: 1.2,//当前视角的缩放比例。
                itemStyle: {
                    normal: {
                        borderWidth: .5, //区域边框宽度
                        borderColor: '#A0BBD6', //区域边框颜色
                        areaColor: "#B6D8F6", //区域颜色
                    },
                    emphasis: {//鼠标滑过地图高亮的相关设置
                        borderWidth: .5,
                        borderColor: '#A8CCE9',
                        areaColor: "#73ADF9",
                    }
                }
            }]
        }
        //省份柱状图拆线图
        this.provinceOption = {
            color: ['#3398DB'],
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    name: '所属市',
                    // data: ['广州市', '深圳市', '中山市', '东莞市', '惠州市', '河源市', '韶关市'],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '单位：元'

                }
            ],
            toolbox: {
                show: true,
                zlevel:0,                                   //所属图形的Canvas分层，zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面
                z:2,
                orient: 'horizontal',
                left: 'right',
                top: '0px',
                feature: {
                    // mark: {show: true},
                    //dataView: {show: true, readOnly: false},
                    myTool1: {
                        show: true,
                        title: '切换为饼图',
                        icon: 'path://M556,65.8C781.9,75.1,961,261.2,961,488c0,69.8-17,137.2-48.9,197.6l-8.2,15.6L538.4,488v-423L556,65.8z\n' +
                            '\t\t\t M572.2,468.6L890.1,654c24.3-51.4,37.1-107.8,37.1-166c0-202.8-155.6-370.2-355-387.3V468.6z M855.2,735.4l-9.5,14.8\n' +
                            '\t\t\tc-77.2,120.3-210.2,194.6-355.9,194.6c-233.4,0-422.6-189.2-422.6-422.6c0-226.8,179.1-413,405-422.2l17.6-0.7v423L855.2,735.4z\n' +
                            '\t\t\t M101,522.2C101,736.9,275.1,911,489.8,911c128,0,245.4-62.3,317.6-164.4L456,541.6V134.8C256.5,152,101,319.4,101,522.2z',

                        onclick: function (){
                            map.createPieMap()
                        }
                    },
                    magicType: {show: true, type: ['line', 'bar','pie']},
                    // restore: {show: true},
                    saveAsImage: {show: true},
                    // dataZoom:{
                    //     show:true
                    // },
                },


            },
            series: [
                {
                    name: '余额_折人民币',
                    type: 'bar',
                    barWidth: '40%',
                    // data: [10, 52, 200, 334, 390, 330, 220]
                }
            ]
        };
        // 省份饼图
        this.provincePieOption = {
            title : {//标题
                //  text: '饼图的标题',//主标题
                subtext: '单位：元',//fubiaoti
                left:"40%",
                top:'60',
                subtextStyle:{
                    color:'#333',
                    fontWeight:'bold'
                }
            },
            tooltip : {//提示
                trigger: 'item',//触发提示的类型：数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
                formatter: "{a} <br/>{b} : {c} ({d}%)"//提示框浮层内容格式器，支持字符串模板和回调函数两种形式。
            },
            toolbox: {
                show: true,
                zlevel:0,                                   //所属图形的Canvas分层，zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面
                z:2,
                orient: 'horizontal',
                left: 'right',
                top: '0px',
                feature: {
                    // mark: {show: true},
                    //dataView: {show: true, readOnly: false},
                    myTool1: {
                        show: true,
                        title: '切换为饼图',
                        icon: 'path://M556,65.8C781.9,75.1,961,261.2,961,488c0,69.8-17,137.2-48.9,197.6l-8.2,15.6L538.4,488v-423L556,65.8z\n' +
                            '\t\t\t M572.2,468.6L890.1,654c24.3-51.4,37.1-107.8,37.1-166c0-202.8-155.6-370.2-355-387.3V468.6z M855.2,735.4l-9.5,14.8\n' +
                            '\t\t\tc-77.2,120.3-210.2,194.6-355.9,194.6c-233.4,0-422.6-189.2-422.6-422.6c0-226.8,179.1-413,405-422.2l17.6-0.7v423L855.2,735.4z\n' +
                            '\t\t\t M101,522.2C101,736.9,275.1,911,489.8,911c128,0,245.4-62.3,317.6-164.4L456,541.6V134.8C256.5,152,101,319.4,101,522.2z',
                        onclick: function (){
                            map.createPieMap()
                        },
                        magicType: {show: true, type: ['line']},
                    },
                    myTool2: {
                        show: true,
                        title: '切换为折线图',
                       icon: 'path://M549.1,628.9c55.5,38.9,116.3-21.9,77.4-77.4c-1.2-1.8-2.8-3.3-4.6-4.6c-55.5-38.9-116.3,21.9-77.4,77.4\n' +
                           '\t\t\tC545.8,626.1,547.4,627.7,549.1,628.9z M300.4,380.2c55.5,38.9,116.3-21.9,77.4-77.4c-1.2-1.8-2.8-3.3-4.6-4.6\n' +
                           '\t\t\tc-55.5-38.9-116.3,21.9-77.4,77.4C297.1,377.4,298.6,378.9,300.4,380.2z M512.6,537.2L396.2,405.3c-15.7,14.1-36.5,22.7-59.4,22.7\n' +
                           '\t\t\tc-28.3,0-53.4-13.2-69.7-33.8L96.8,568.8l-25.5-24.7l179.3-183.7c-5.6-23.1-2.2-49.8,14.2-76.1c4.4-7,10.3-12.9,17.3-17.2\n' +
                           '\t\t\tc70.3-43.9,143.6,5.6,143.6,72.2c0,13.1-2.8,25.6-8,36.8l120.7,136.7c13.7-8.6,29.8-13.5,47.2-13.5c13.4,0,26,2.9,37.4,8.2\n' +
                           '\t\t\tl292.8-309.6c6.7-7.1,18-7.4,25.1-0.7v0c7.1,6.7,7.4,18,0.7,25.1L651.7,528.7c24.9,27.8,32.6,71.3,6.2,113.8\n' +
                           '\t\t\tc-4.4,7.2-10.4,13.2-17.6,17.7c-70.3,43.9-143.6-5.6-143.6-72.2C496.7,569.1,502.6,551.6,512.6,537.2z M940.9,890\n' +
                           '\t\t\tc15.5,0,26.4,19.9,8.5,35.5H66.1l0-808c15.7-17.8,35.5-7,35.5,8.5v764H940.9z',
                        onclick: function (param){
                            map.createProvinceMap(false,param);
                        }
                    },
                    myTool3: {
                        show: true,
                        title: '切换为柱状图',
                        icon: 'path://M717.9,316.5h107.5c29.7,0,53.7,24.1,53.7,53.7v537.3c0,29.7-24.1,53.7-53.7,53.7H717.9\n' +
                            '\t\t\tc-29.7,0-53.7-24.1-53.7-53.7V370.3C664.2,340.6,688.2,316.5,717.9,316.5z M717.9,352.4c-9.9,0-17.9,8-17.9,17.9v537.3\n' +
                            '\t\t\tc0,9.9,8,17.9,17.9,17.9h107.5c9.9,0,17.9-8,17.9-17.9V370.3c0-9.9-8-17.9-17.9-17.9H717.9z M431.3,65.8h107.5\n' +
                            '\t\t\tc29.7,0,53.7,24.1,53.7,53.7v788c0,29.7-24.1,53.7-53.7,53.7H431.3c-29.7,0-53.7-24.1-53.7-53.7v-788\n' +
                            '\t\t\tC377.6,89.9,401.7,65.8,431.3,65.8z M431.3,101.6c-9.9,0-17.9,8-17.9,17.9v788c0,9.9,8,17.9,17.9,17.9h107.5\n' +
                            '\t\t\tc9.9,0,17.9-8,17.9-17.9v-788c0-9.9-8-17.9-17.9-17.9H431.3z M144.8,531.4h107.5c29.7,0,53.7,24.1,53.7,53.7v322.4\n' +
                            '\t\t\tc0,29.7-24.1,53.7-53.7,53.7H144.8c-29.7,0-53.7-24.1-53.7-53.7V585.2C91.1,555.5,115.1,531.4,144.8,531.4z M144.8,567.3\n' +
                            '\t\t\tc-9.9,0-17.9,8-17.9,17.9v322.4c0,9.9,8,17.9,17.9,17.9h107.5c9.9,0,17.9-8,17.9-17.9V585.2c0-9.9-8-17.9-17.9-17.9H144.8z',
                        onclick: function (param){
                            map.createProvinceMap(true,param);
                        }
                    },
                    // magicType: {show: true, type: ['line', 'bar','pie']},
                    // restore: {show: true},
                    saveAsImage: {show: true},
                    // dataZoom:{
                    //     show:true
                    // },
                }
            },
            legend: {//图例组件。
                type: 'scroll',
                orient: 'vertical',
                right: 60,
                top: 100,
                bottom: 20,
                icon: "circle",
                itemWidth:20,  // 设置宽度
                itemHeight:20, // 设置高度
                // data: ['广州市', '深圳市', '中山市', '东莞市', '惠州市', '河源市', '韶关市'],
                /**
                 * 图例的数据数组。数组项通常为一个字符串，每一项代表一个系列的 name
                 * （如果是饼图，也可以是饼图单个数据的 name）。图例组件会自动根据对
                 * 应系列的图形标记（symbol）来绘制自己的颜色和标记，特殊字符串 ''
                 * （空字符串）或者 '\n'（换行字符串）用于图例的换行。
                 如果 data 没有被指定，会自动从当前系列中获取。
                 多数系列会取自 series.name 或者 series.encode 的 seriesName 所指定的维度。
                 如 饼图 and 漏斗图 等会取自 series.data 中的 name。
                 如果要设置单独一项的样式，也可以把该项写成配置项对象。
                 此时必须使用 name 属性对应表示系列的 name。
                 */
            },
            series : [ //系列列表。每个系列通过 type 决定自己的图表类型
                {
                    name: '提示信息',
                    type: 'pie',
                    /**
                     * 饼图主要用于表现不同类目的数据在总和中的占比。
                     * 每个的弧度表示数据数量的比例。
                     */
                    radius : '55%',
                    center: ['50%', '60%'],
                    // data:[
                    //     {value:10, name:'广州市'},
                    //     {value:52, name:'深圳市'},
                    //     {value:200, name:'中山市'},
                    //     {value:334, name:'东莞市'},
                    //     {value:390, name:'惠州市'},
                    //     {value:330, name:'河源市'},
                    //     {value:220, name:'韶关市'}
                    // ],
                    itemStyle: {//图形样式。
                        emphasis: {
                            shadowBlur: 10,//图形阴影的模糊大小。
                            shadowOffsetX: 0,//阴影水平方向上的偏移距离。
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                            /**
                             * 颜色可以使用 RGB 表示，比如 'rgb(128, 128, 128)'，
                             * 如果想要加上 alpha 通道表示不透明度，可以使用 RGBA，
                             * 比如 'rgba(128, 128, 128, 0.5)'，
                             * 也可以使用十六进制格式，比如 '#ccc'。
                             * 除了纯色之外颜色也支持渐变色和纹理填充
                             */
                        },
                        normal:{
                            label:{
                                show: true,
                                formatter: '{b} : {c} ({d}%)'
                            },
                            labelLine :{show:true}
                        }

                    }
                }
            ]
        }
        this.lang = {
            sProcessing: "<div class='loader'>加载中...</div>",//加载中...
            // sProcessing: "<div class='loader'>bt.lang('common.table.loading')</div>",//加载中...
            //sProcessing: "<div class='loader'><i class='layui-icon layui-anim layui-anim-rotate layui-anim-loop' style='font-size: 40px'></i></div>",//
            sLengthMenu:bt.lang('common.table.lengthMenu') ,//"每页 _MENU_ 项",
            // sZeroRecords: "换个搜索词试试呢？暂无数据",
            sZeroRecords:'暂无数据',//bt.lang('common.msg.temporarily_no_data') ,//"暂无数据",
            sInfo:bt.lang('common.table.info') ,//"当前显示第 _START_ 至 _END_ 项，全部 _TOTAL_ 项。",
            sInfoEmpty:bt.lang('common.table.infoEmpty') ,//"当前显示第 0 至 0 项，全部 0 项",
            sInfoFiltered:bt.lang('common.table.infoFiltered') ,//"(由 _MAX_ 项结果过滤)",
            sInfoPostFix: "",
            sSearch: "本地搜索：",
            sUrl: "",
            sEmptyTable:bt.lang('common.msg.temporarily_no_data') ,//"暂无数据",
            sLoadingRecords:bt.lang('common.table.loading'),//"载入中...",
            sInfoThousands: ",",
            oPaginate: {
                sFirst:bt.lang('common.table.firstPage'),//"首页",
                //sPrevious:bt.lang('common.table.previousPage') ,//"上一页",
                sPrevious:'< ' ,//"上一页",
                //sNext:bt.lang('common.table.nextPage') ,//"下一页",
                sNext:'> ' ,//"下一页",
                sLast: bt.lang('common.table.lastPage'),//"最后页",
                sJump:bt.lang('common.table.jump'),//"跳转至"
            },
            oAria: {
                sSortAscending:bt.lang('common.table.sortAscending'),//":以升序排列此列",
                sSortDescending:bt.lang('common.table.sortDescending') //": 以降序排列此列"
            }
        };

    }
    Map.fn = Map.prototype
    /**
     * 参数设置
     * @param {Object} options
     */
    Map.fn.set = function(options){
        var that = this;
        $.extend(true, that.config, options);
        return that
    }
    //初始化
    Map.fn.render = function () {
        var that = this;
        element.on('nav(currencies)', function(elem){ //币种选择方法
            $("#currencies-selected").html($(elem).text());
            that.updateWorldMapData();
            that.myChart.setOption(that.option);
        });
        that.onMap();
        that.closeMap();
        that.onRefreshMap();
        that.updateWorldMapData();
        that.myChart.setOption(this.option);
        that.refreshDate();
    }

    Map.fn.onMap = function () {
        var that = this;
        if(!that.myChart){
            that.myChart = echarts.init(document.getElementById('main'));
            that.myChart.on("click", function (param) { //点击事件
                // 只有中国才穿透
                if (param.value == 5) {
                    $('#map-china').removeClass('hide');
                    that.updateChinaMapData()
                    that.myChart.setOption(that.chinaOption,true);
                    $("#currencies").addClass("hide");//隐藏下拉选择币类
                    $('#map-header').removeClass('is-world');
                }
                // 点击省份时执行代码
                if(param.seriesId =='chinaMap'){
                    var provinceData = that.config.clickChinaMapCallback(param,that.myChart);
                    if (provinceData.length){
                        that.updateProvinceMapData(provinceData)
                        that.createProvinceMap(true,param)
                        that.createTable(provinceData);
                    } else {
                        layer.alert(param.name +'余额为0')
                    }
                }
            })
            that.clickMapHeaderText();
            that.myChart.showLoading();
        }
    }
    //注册地图点击左边文字的回调
    Map.fn.clickMapHeaderText = function () {
        var that = this;
        $('#map-header .item-header-text').off('click').on('click',function(){
            var name = $(this).data('name');
            if(name =='china'){
                $('#main').removeClass('hide');
                $('#draw-province').addClass('hide');
                that.updateChinaMapData();
                that.myChart.setOption(that.chinaOption,true);
                $('#map-province').addClass('hide');
                $("#currencies").addClass("hide");//显示下拉选择币类
                $('#map-header').removeClass('is-world');
            }else if(name =='world') {
                $('#main').removeClass('hide');
                $('#draw-province').addClass('hide');
                $('#map-china').addClass('hide');
                $('#map-province').addClass('hide');
                $("#currencies").removeClass("hide");//显示下拉选择币类
                $('#map-header').addClass('is-world');
                that.updateWorldMapData();
                that.myChart.setOption(that.option,true)
            }
        })
    }

    //点击关闭钮，可以关闭省及中国地图
    Map.fn.closeMap = function(){
        $('#closeMap').on('click',function () {
            var isＮotProvince = $('#map-province').hasClass('hide');
            if (isＮotProvince){ //说明是中国地图，关闭时转到世界地图
                $('#map-header').children(":first").trigger('click');
            } else {　//说明是省份，关闭时，要转到中国地图
                $('#map-china .item-header-text').trigger('click');
            }
        })
    }
    Map.fn.onRefreshMap = function(){
        var that = this;
        $('#refreshMap').on('click',function(){
            var isChina = $('#map-china').hasClass('hide');
            var isProvince = $('#map-province').hasClass('hide');
            bt.loading.show();
           // that.isAjaxing = true;
            if(!isChina){
                if(!isProvince){ // 省份数据
                   // that.myProvinceChart.showLoading();
                    var name = $('#province').text();
                    var provinceData = that.config.clickChinaMapCallback({name:name},that.myChart);
                    that.updateProvinceMapData(provinceData);
                    that.createProvinceMap(true,{name:name});
                    //that.myProvinceChart.hideLoading();
                    that.createTable(provinceData);
                }else { // 中国地图
                    //that.myChart.showLoading();
                    that.updateChinaMapData();
                   // that.myChart.hideLoading();
                    that.myChart.setOption(that.chinaOption,true);
                }
            }else { //世界地图
               // that.myChart.showLoading();
                that.updateWorldMapData();
               // that.myChart.hideLoading();
                that.myChart.setOption(that.option,true)
            }
            //that.isAjaxing = false;
            that.refreshDate();
            bt.loading.hide();
        })
    }
    // 更新中国地图数据
    Map.fn.updateChinaMapData = function (moneyUnit) {
        var that = this;
        $.ajax({
            type: "post",
            url: basePath + "/bi/synthesize/moneyMap/getChinaMapData",
            async: false, //同步执行
            data: {
                "moneyUnit": moneyUnit
            },
            dataType: "json", //返回数据形式为json
            success: function (result) {
                that.chinaOption.series[0].data = JSON.parse(result.data);
                that.myChart.hideLoading();
            }
        });
    }

    //更新世界地图数据
    Map.fn.updateWorldMapData = function () {
        var that = this;
        var moneyUnit = $('#currencies').find('.layui-this').data('value');
        $.ajax({
            type: "post",
            url: basePath + "/bi/synthesize/moneyMap/getWorldMapData",
            async: false, //同步执行
            data: {
                "moneyUnit": moneyUnit
            },
            dataType: "json", //返回数据形式为json
            success: function (result) {
                that.option.series[0].data = JSON.parse(result.data);
                if(that.myChart){
                    that.myChart.hideLoading();
                }
            }
        });
    }
    // 更新省地图数据
    Map.fn.updateProvinceMapData = function (province) {
        var data = [],city = [];
        for(var i = 0; i <  province.length; i++ ){
            data.push(province[i].value);
            city.push(province[i].name)
        }
        this.provinceOption.xAxis[0].data = city;
        this.provinceOption.series[0].data = data;
        this.provincePieOption.legend.data = city;
        this.provincePieOption.series[0].data = province;
    }
    // 创建表格
    Map.fn.createTable = function (option) {
        var config = {
            language: this.lang,
            dom: 'frtlp',
            ordering: false,
            serverSide: false,
            scrollX:'auto',
            scrollY:'220px',
            paging: false,
            data: this.initTableData(option),
            columns:[
                {data:'name'},
                {data:'currency'},
                {data:'value_currency',render:function (full) {
                        return bt.money.format(full)
                    }},
                {data:'value_CNY',render:function (full) {
                        return bt.money.format(full)
                    }}
            ]
        }
        if(this.table){
            this.table.destroy();
            this.table = null;
        }
        this.table = $('#mapTable').DataTable(config)
    }

    Map.fn.initTableData = function (data) {
        var arr = [];
        if(data){
            for(var i =0,length = data.length; i < length; i++ ){
                var obj = {name:data[i].name,value:data[i].value}
                for(var j = 0,len = data[i].capital.length; j < len; j++ ){
                    arr.push($.extend(true,{},obj,data[i].capital[j]));
                }
            }
        }

        return arr;
    }

    Map.fn.createPieMap = function () {
        $('#province1').addClass('hide');
        $('#province2').removeClass('hide');
        if(!this.myPieChart){
            this.myPieChart = echarts.init(document.getElementById('province2'));
        }
        this.myPieChart.setOption(this.provincePieOption);
    }

    Map.fn.createProvinceMap = function (isBar,param) {
        $('#main').addClass('hide');
        $('#draw-province').removeClass('hide');
        $('#province1').removeClass('hide');
        $('#province2').addClass('hide');
        if(!this.myProvinceChart){
            this.myProvinceChart = echarts.init(document.getElementById('province1'));
        }
        if(isBar){
            this.provinceOption.series[0].type = 'bar';
        }else{
            this.provinceOption.series[0].type = 'line';
        }
        this.myProvinceChart.setOption(this.provinceOption,true);
        $('#map-province').removeClass('hide')
        $('#province').text(param.name)
    }

    Map.fn.refreshDate = function () {
        var date = bt.date.format(new Date,'yyyy-MM-dd hh:mm:ss');
        $('#mapDate').text(date)
    }
    function throwError(msg) {
        throw new Error(msg);
    };
    var map = new Map();
    var obj = {
        init:function(ops){
            map.set(ops);
            map.render();
            return this
        }
    }
    //输入口
    exports('worldMap', obj);
}).addcss('../../../css/worldMap.css','worldMap')
