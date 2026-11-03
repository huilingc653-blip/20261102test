/**
 * 银行帐号格式化组件
 * 本组件依赖echart / world / china
 * liuyj
 * 2020年02月24日
 * 0.0.1 世界地图
 * 0.0.2 国际化
 * 0.0.3 柱状图排序
 * 0.0.4 新增自适应功能
 * 0.0.5 优化柱状图、折线图、饼图、下载图标

 */
layui.define(['jquery','form','datatablecore','date','element'], function(exports){
    var $ = layui.jquery,form = layui.form(),element = layui.element();
    form.render('select');

    var Map =function(){
        this.config={
            clickChinaMapCallback:undefined,
        };
        this.myChart = null;
        this.myProvinceChart = null;
        this.myPieChart = null;
        this.table = null;
        this.continentMapArr = [ //大洲信息
            {map:'southAmerica',name:bt.lang('bi.money.map.southAmerica'),color: '#B9C594'},
            {map:'northAmerica',name:bt.lang('bi.money.map.northAmerica'),color: '#B9C594'},
            {map:'africa',name:bt.lang('bi.money.map.africa'),color: '#AAC8ED'},
            {map:'southEasternAsia',name:bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),color: '#E2D5BC'},
            {map:'othersAsia',name:bt.lang('bi.money.map.otherPartsOfAsia'),color: '#B3D6D1'},
            {map:'oceania',name:bt.lang('bi.money.map.oceania'), color: '#9DB6BA'},
            {map:'europe',name:bt.lang('bi.money.map.europe'),color: '#D2D2E7'}
        ];
        //所有国家的大洲定义
       this.continentCountrys = {
            'China': bt.lang('bi.money.map.china'),
            'Taiwan':bt.lang('bi.money.map.china'),
            'Japan': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'North Korea': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),//朝鲜
            'Brunei': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Timor-Leste': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'South Korea': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Malaysia': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Myanmar': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Vietnam': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Thailand': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Cambodia': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Philippines': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Lao PDR': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Indonesia': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'India': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Bhutan': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Nepal': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Bangladesh': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Sri Lanka': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Pakistan': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Laos': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Afghanistan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Iran': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Turkmenistan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Tajikistan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Kyrgyzstan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Kazakhstan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Mongolia': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Uzbekistan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'United Arab Emirates': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Oman': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Yemen': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Saudi Arabia': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Jordan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Iraq': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Syria': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Qatar': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Azerbaijan': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Turkey': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Armenia': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Georgia': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Israel': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Palestine': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Kuwait': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Lebanon': bt.lang('bi.money.map.otherPartsOfAsia'),
            'East Timor': bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),
            'Cyprus': bt.lang('bi.money.map.otherPartsOfAsia'),
            'Russian Federation': bt.lang('bi.money.map.europe'),
            'Finland': bt.lang('bi.money.map.europe'),
            'Estonia': bt.lang('bi.money.map.europe'),
            'Latvia': bt.lang('bi.money.map.europe'),
            'Lithuania': bt.lang('bi.money.map.europe'),
            'Ukraine': bt.lang('bi.money.map.europe'),
            'Belarus': bt.lang('bi.money.map.europe'),
            'Moldova': bt.lang('bi.money.map.europe'),
            'Bulgaria': bt.lang('bi.money.map.europe'),
            'Romania': bt.lang('bi.money.map.europe'),
            'Greece': bt.lang('bi.money.map.europe'),
            'The Former Yugoslav Republic of Macedonia': bt.lang('bi.money.map.europe'),
            'Albania': bt.lang('bi.money.map.europe'),
            'Montenegro': bt.lang('bi.money.map.europe'),
            'Serbia': bt.lang('bi.money.map.europe'),
            'Bosnia and Herz.': bt.lang('bi.money.map.europe'),
            'Croatia': bt.lang('bi.money.map.europe'),
            'Sweden': bt.lang('bi.money.map.europe'),
            'Norway': bt.lang('bi.money.map.europe'),
            'Denmark': bt.lang('bi.money.map.europe'),
            'Poland': bt.lang('bi.money.map.europe'),
            'Slovakia': bt.lang('bi.money.map.europe'),
            'Hungary': bt.lang('bi.money.map.europe'),
            'Czech Republic': bt.lang('bi.money.map.europe'),
            'Austria': bt.lang('bi.money.map.europe'),
            'Slovenia': bt.lang('bi.money.map.europe'),
            'Italy': bt.lang('bi.money.map.europe'),
            'Switzerland': bt.lang('bi.money.map.europe'),
            'Germany': bt.lang('bi.money.map.europe'),
            'Netherlands': bt.lang('bi.money.map.europe'),
            'Belgium': bt.lang('bi.money.map.europe'),
            'France': bt.lang('bi.money.map.europe'),
            'Spain': bt.lang('bi.money.map.europe'),
            'Portugal': bt.lang('bi.money.map.europe'),
            'Britain': bt.lang('bi.money.map.europe'),
            'Ireland': bt.lang('bi.money.map.europe'),
            'Iceland': bt.lang('bi.money.map.europe'),
            'Kosovo': bt.lang('bi.money.map.europe'),
            'Bosnia and Herzegovina': bt.lang('bi.money.map.europe'),
            'Luxembourg': bt.lang('bi.money.map.europe'),
            'Australia': bt.lang('bi.money.map.oceania'),
            'Papua New Guinea': bt.lang('bi.money.map.oceania'),
            'New Zealand': bt.lang('bi.money.map.oceania'),
            'New Caledonia': bt.lang('bi.money.map.oceania'),
            'Solomon Islands': bt.lang('bi.money.map.oceania'),
            'Fiji': bt.lang('bi.money.map.oceania'),
            'Vanuatu': bt.lang('bi.money.map.oceania'),
            'United States': bt.lang('bi.money.map.northAmerica'),
            'Canada': bt.lang('bi.money.map.northAmerica'),
            'Greenland': bt.lang('bi.money.map.northAmerica'),
            'Guatemala': bt.lang('bi.money.map.southAmerica'),
            'Belize': bt.lang('bi.money.map.southAmerica'),
            'Honduras': bt.lang('bi.money.map.southAmerica'),
            'El Salvador': bt.lang('bi.money.map.southAmerica'),
            'Panama': bt.lang('bi.money.map.southAmerica'),
            'Mexico': bt.lang('bi.money.map.northAmerica'),
            'Cuba': bt.lang('bi.money.map.southAmerica'),
            'Nicaragua': bt.lang('bi.money.map.southAmerica'),
            'Costa Rica': bt.lang('bi.money.map.southAmerica'),
            'Jamaica': bt.lang('bi.money.map.southAmerica'),
            'Haiti': bt.lang('bi.money.map.southAmerica'),
            'Bahamas':bt.lang('bi.money.map.southAmerica'),
            'Dominican Repblic': bt.lang('bi.money.map.southAmerica'),
            'Puerto Rico': bt.lang('bi.money.map.southAmerica'),
            'Egypt': bt.lang('bi.money.map.africa'),
            'Libya': bt.lang('bi.money.map.africa'),
            'Tunisia': bt.lang('bi.money.map.africa'),
            'Algeria': bt.lang('bi.money.map.africa'),
            'Morocco': bt.lang('bi.money.map.africa'),
            'Gambia':bt.lang('bi.money.map.africa'),
            'Sudan': bt.lang('bi.money.map.africa'),
            'Eritrea': bt.lang('bi.money.map.africa'),
            'Somalia': bt.lang('bi.money.map.africa'),
           'Somaliland': bt.lang('bi.money.map.africa'),
            'Ethiopia': bt.lang('bi.money.map.africa'),
            'South Sudan': bt.lang('bi.money.map.africa'),
            'Chad': bt.lang('bi.money.map.africa'),
            'Niger': bt.lang('bi.money.map.africa'),
            'Mali': bt.lang('bi.money.map.africa'),
            'Western Sahara': bt.lang('bi.money.map.africa'),
            'Mauritania': bt.lang('bi.money.map.africa'),
            'Senegal': bt.lang('bi.money.map.africa'),
            'Guinea': bt.lang('bi.money.map.africa'),
            'Guinea-Bissau': bt.lang('bi.money.map.africa'),
            'Burkina Faso': bt.lang('bi.money.map.africa'),
            'Ghana': bt.lang('bi.money.map.africa'),
            'Togo': bt.lang('bi.money.map.africa'),
            'Benin': bt.lang('bi.money.map.africa'),
            'Liberia': bt.lang('bi.money.map.africa'),
            "Cote d'Ivoire": bt.lang('bi.money.map.africa'),
            'Sierra Leone': bt.lang('bi.money.map.africa'),
            'Equatorial Guinea': bt.lang('bi.money.map.africa'),
            'Djibouti': bt.lang('bi.money.map.africa'),
            'Central African Republic': bt.lang('bi.money.map.africa'),
            'Congo-kinshasa': bt.lang('bi.money.map.africa'),
            'Gabon': bt.lang('bi.money.map.africa'),
            'Republic of the Congo': bt.lang('bi.money.map.africa'),
            'Uganda': bt.lang('bi.money.map.africa'),
            'Tanzania': bt.lang('bi.money.map.africa'),
            'Rwanda': bt.lang('bi.money.map.africa'),
            'Burundi': bt.lang('bi.money.map.africa'),
            'Kenya': bt.lang('bi.money.map.africa'),
            'Angola': bt.lang('bi.money.map.africa'),
            'Namibia': bt.lang('bi.money.map.africa'),
            'South Africa': bt.lang('bi.money.map.africa'),
            'Cameroon': bt.lang('bi.money.map.africa'),
            'Botswana': bt.lang('bi.money.map.africa'),
            'Zambia': bt.lang('bi.money.map.africa'),
            'Zimbabwe': bt.lang('bi.money.map.africa'),
            'Malawi': bt.lang('bi.money.map.africa'),
            'Mozambique': bt.lang('bi.money.map.africa'),
            'Lesotho': bt.lang('bi.money.map.africa'),
            'Nigeria': bt.lang('bi.money.map.africa'),
            'Guinea-Bissau': bt.lang('bi.money.map.africa'),
            'Swaziland': bt.lang('bi.money.map.africa'),
            'Madagascar': bt.lang('bi.money.map.africa'),
            'Equatorial Guinea': bt.lang('bi.money.map.africa'),
            'Colombia': bt.lang('bi.money.map.southAmerica'),
            'Venezuela': bt.lang('bi.money.map.southAmerica'),
            'Guyana': bt.lang('bi.money.map.southAmerica'),
            'Suriname': bt.lang('bi.money.map.southAmerica'),
            'Brazil': bt.lang('bi.money.map.southAmerica'),
            'Ecuador': bt.lang('bi.money.map.southAmerica'),
            'Peru': bt.lang('bi.money.map.southAmerica'),
            'Bolivia': bt.lang('bi.money.map.southAmerica'),
            'Chile': bt.lang('bi.money.map.southAmerica'),
            'Argentina': bt.lang('bi.money.map.southAmerica'),
            'Uruguay': bt.lang('bi.money.map.southAmerica'),
            'Paraguay': bt.lang('bi.money.map.southAmerica'),
            'French Guiana': bt.lang('bi.money.map.southAmerica'),
        }
        //世界地图信息
        this.option = {
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    var isEn = top.webLocale == 'en_US'; //系统语言
                    var name = params.name;
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
                // categories: [ bt.lang('bi.money.map.northAmerica'),bt.lang('bi.money.map.southAmerica'),bt.lang('bi.money.map.europe'),bt.lang('bi.money.map.otherPartsOfAsia'),'中国',bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),bt.lang('bi.money.map.africa'),bt.lang('bi.money.map.oceania')],
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
                    // nameMap: {
                    //     selectedMode: 'multiple',
                    //
                    // },
                }
            ],
            dataRange: {
                splitList: [
                    {start: 1, end: 1, label: bt.lang('bi.money.map.northAmerica'), color: '#B9C594'},
                    {start: 2, end: 2, label: bt.lang('bi.money.map.southAmerica'), color: '#E7C772'},
                    {start: 3, end: 3, label: bt.lang('bi.money.map.europe'), color: '#D2D2E7'},
                    {start: 4, end: 4, label: bt.lang('bi.money.map.otherPartsOfAsia'), color: '#B3D6D1'},
                    {start: 5, end: 5, label: bt.lang('bi.money.map.china'), color: '#E38B78'},
                    {start: 6, end: 6, label: bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'), color: '#E2D5BC'},
                    {start: 7, end: 7, label: bt.lang('bi.money.map.africa'), color: '#AAC8ED'},
                    {start: 8, end: 8, label: bt.lang('bi.money.map.oceania'), color: '#9DB6BA'},
                ]
            }
        };
        //大洲信息集
        this.continentOptions = {
            // bt.lang('bi.money.map.southAmerica'):{},
            // bt.lang('bi.money.map.northAmerica'):{},
            // bt.lang('bi.money.map.europe'):{},
            // bt.lang('bi.money.map.otherPartsOfAsia'):{},
            // bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'):{},
            // bt.lang('bi.money.map.africa'):{},
            // bt.lang('bi.money.map.oceania'):{}
        }

        //大洲公共的信息集
        this.modelCommonOption ={
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    var name = '',isEn = top.webLocale == 'en_US'; //系统语言
                    if (!isEn){
                        name = params.data.name_zh
                    }else {
                        name = params.data.name_en
                    }
                    var html = '<div class="area-title">' + name + '</div>', data = params.data.data;
                    if (data && data.length) {
                        for (var i = 0; i < data.length; i++) {
                            html = html + '<div class="layui-clear"><span class="area-name">' + data[i].name + '</span><span class="area-value">' + data[i].value + '</span></div>';
                        }
                    }
                    return html
                }
            },
            visualMap: {
                // min: 1,
                // max: 10,
                min: 0,
                max: 38000000,
                text: ['High', 'Low'],
                realtime: false,
                calculable: true,
                show:false,
                // categories: [ bt.lang('bi.money.map.northAmerica'),bt.lang('bi.money.map.southAmerica'),bt.lang('bi.money.map.europe'),bt.lang('bi.money.map.otherPartsOfAsia'),'中国',bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea'),bt.lang('bi.money.map.africa'),bt.lang('bi.money.map.oceania')],
                // inRange: {
                //     color: ['#B9C594', '#E7C772','#D2D2E7','#B3D6D1','#E38B78','#E2D5BC','#AAC8ED','#9DB6BA'],
                // },
                 color: ['#E38B78','yellow','#B9C594']
            },
            series: [
                {
                    name: '',
                    type: 'map',
                    mapType: '',

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
                    // center: [115.97, 29.71],
                    aspectScale: 0.75,
                    zoom:1.2,
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
                    right:100,
                    data: [
                        // {name: 'Afghanistan',value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: "Côte d'Ivoire",value:28397.812, data:[{name:'美元',value:28397.812}]},
                        // {name: "CÃ´te d'Ivoire", data:[{name:'美元',value:28397.812}]},
                        // {name: 'Angola', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Albania', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'United Arab Emirates',value:28397.812, data:[{name:'美元',value:28397.812}]},
                        // {name: 'Argentina', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Armenia', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'French Southern and Antarctic Lands', data:[{name:'美元',value:28397.812}]},
                        // {name: 'Australia', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Austria', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Azerbaijan',value:28397.812, data:[{name:'美元',value:28397.812}]},
                        // {name: 'Burundi', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Belgium', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Benin', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Burkina Faso',value:28397.812, data:[{name:'美元',value:28397.812}]},
                        // {name: 'Bangladesh', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'Bulgaria', value:28397.812,data:[{name:'美元',value:28397.812}]},
                        // {name: 'The Bahamas',value:28397.812, data:[{name:'美元',value:28397.812}]},
                        // {name: 'Bosnia and Herzegovina',value:28397.812, data:[{name:'美元',value:28397.812}]},
                        // {name: 'Belarus', value: 9491.07},
                        // {name: 'Belize', value: 308.595},
                        // {name: 'Bermuda', value: 64.951},
                        // {name: 'Bolivia', value: 716.939},
                        // {name: 'Brazil', value: 195210.154},
                        // {name: 'Brunei', value: 27.223},
                        // {name: 'Bhutan', value: 716.939},
                        // {name: 'Botswana', value: 1969.341},
                        // {name: 'Central African Republic', value: 4349.921},
                        // {name: 'Central African Rep.', value: 4349.921},
                        // {name: 'Canada', value: 34126.24},
                        // {name: 'Switzerland', value: 7830.534},
                        // {name: 'Chile', value: 17150.76},
                        // {name: 'China', value: 1359821.465},
                        // {name: 'Ivory Coast', value: 60508.978},
                        // {name: 'Cameroon', value: 20624.343},
                        // {name: 'Democratic Republic of the Congo', value: 62191.161},
                        // {name: 'Republic of the Congo', value: 3573.024},
                        // {name: 'Dem. Rep. Congo', value: 3573.024},
                        // {name: 'Congo', value: 3573.024},
                        // {name: 'Colombia', value: 46444.798},
                        // {name: 'Costa Rica', value: 4669.685},
                        // {name: 'Cuba', value: 11281.768},
                        // {name: 'Northern Cyprus', value: 1.468},
                        // {name: 'Cyprus', value: 1103.685},
                        // {name: 'Czech Republic', value: 10553.701},
                        // {name: 'Germany', value: 83017.404},
                        // {name: 'Djibouti', value: 834.036},
                        // {name: 'Denmark', value: 5550.959},
                        // {name: 'Dominican Republic', value: 10016.797},
                        // {name: 'Algeria', value: 37062.82},
                        // {name: 'Ecuador', value: 15001.072},
                        // {name: 'Egypt', value: 78075.705},
                        // {name: 'Eritrea', value: 5741.159},
                        // {name: 'Spain', value: 46182.038},
                        // {name: 'Estonia', value: 1298.533},
                        // {name: 'Ethiopia', value: 87095.281},
                        // {name: 'Finland', value: 5367.693},
                        // {name: 'Fiji', value: 860.559},
                        // {name: 'Falkland Islands', value: 49.581},
                        // {name: 'France', value: 63230.866},
                        // {name: 'Gabon', value: 1556.222},
                        // {name: 'United Kingdom', value: 62066.35},
                        // {name: 'Georgia', value: 4388.674},
                        // {name: 'Ghana', value: 24262.901},
                        // {name: 'Guinea', value: 10876.033},
                        // {name: 'Gambia', value: 1680.64},
                        // {name: 'Guinea Bissau', value: 10876.033},
                        // {name: 'Guinea-Bissau', value: 10876.033},
                        // {name: 'Eq. Guinea', value: 696.167},
                        // {name: 'Greece', value: 11109.999},
                        // {name: 'Greenland', value: 56.546},
                        // {name: 'Guatemala', value: 14341.576},
                        // {name: 'French Guiana', value: 231.169},
                        // {name: 'Guyana', value: 786.126},
                        // {name: 'Honduras', value: 7621.204},
                        // {name: 'Croatia', value: 4338.027},
                        // {name: 'Haiti', value: 9896.4},
                        // {name: 'Hungary', value: 10014.633},
                        // {name: 'Indonesia', value: 240676.485},
                        // {name: 'India', value: 1205624.648},
                        // {name: 'Ireland', value: 4467.561},
                        // {name: 'Iran', value: 240676.485},
                        // {name: 'Iraq', value: 30962.38},
                        // {name: 'Iceland', value: 318.042},
                        // {name: 'Israel', value: 7420.368},
                        // {name: 'Italy', value: 60508.978},
                        // {name: 'Jamaica', value: 2741.485},
                        // {name: 'Jordan', value: 6454.554},
                        // {name: 'Japan', value: 127352.833},
                        // {name: 'Kazakhstan', value: 15921.127},
                        // {name: 'Kenya', value: 40909.194},
                        // {name: 'Kyrgyzstan', value: 5334.223},
                        // {name: 'Cambodia', value: 14364.931},
                        // {name: 'Korea', value: 51452.352},
                        // {name: 'Kosovo', value: 97.743},
                        // {name: 'Kuwait', value: 2991.58},
                        // {name: 'Laos', value: 6395.713},
                        // {name: 'Lebanon', value: 4341.092},
                        // {name: 'Liberia', value: 3957.99},
                        // {name: 'Libya', value: 6040.612},
                        // {name: 'Sri Lanka', value: 20758.779},
                        // {name: 'Lesotho', value: 2008.921},
                        // {name: 'Lithuania', value: 3068.457},
                        // {name: 'Luxembourg', value: 507.885},
                        // {name: 'Latvia', value: 2090.519},
                        // {name: 'Morocco', value: 31642.36},
                        // {name: 'Moldova', value: 103.619},
                        // {name: 'Madagascar', value: 21079.532},
                        // {name: 'Mexico', value: 117886.404},
                        // {name: 'Macedonia', value: 507.885},
                        // {name: 'Mali', value: 13985.961},
                        // {name: 'Myanmar', value: 51931.231},
                        // {name: 'Montenegro', value: 620.078},
                        // {name: 'Mongolia', value: 2712.738},
                        // {name: 'Mozambique', value: 23967.265},
                        // {name: 'Mauritania', value: 3609.42},
                        // {name: 'Malawi', value: 15013.694},
                        // {name: 'Malaysia', value: 28275.835},
                        // {name: 'Namibia', value: 2178.967},
                        // {name: 'New Caledonia', value: 246.379},
                        // {name: 'Niger', value: 15893.746},
                        // {name: 'Nigeria', value: 159707.78},
                        // {name: 'Nicaragua', value: 5822.209},
                        // {name: 'Netherlands', value: 16615.243},
                        // {name: 'Norway', value: 4891.251},
                        // {name: 'Nepal', value: 26846.016},
                        // {name: 'New Zealand', value: 4368.136},
                        // {name: 'Oman', value: 2802.768},
                        // {name: 'Pakistan', value: 173149.306},
                        // {name: 'Panama', value: 3678.128},
                        // {name: 'Peru', value: 29262.83},
                        // {name: 'Philippines', value: 93444.322},
                        // {name: 'Papua New Guinea', value: 6858.945},
                        // {name: 'Poland', value: 38198.754},
                        // {name: 'Puerto Rico', value: 3709.671},
                        // {name: 'Dem. Rep. Korea', value: 1.468},
                        // {name: 'Portugal', value: 10589.792},
                        // {name: 'Paraguay', value: 6459.721},
                        // {name: 'Qatar', value: 1749.713},
                        // {name: 'Romania', value: 21861.476},
                        // {name: 'Russia', value: 21861.476},
                        // {name: 'Rwanda', value: 10836.732},
                        // {name: 'W. Sahara', value: 514.648},
                        // {name: 'Saudi Arabia', value: 27258.387},
                        // {name: 'Sudan', value: 35652.002},
                        // {name: 'South Sudan', value: 9940.929},
                        // {name: 'S. Sudan', value: 9940.929},
                        // {name: 'Senegal', value: 12950.564},
                        // {name: 'Solomon Islands', value: 526.447},
                        // {name: 'Sierra Leone', value: 5751.976},
                        // {name: 'El Salvador', value: 6218.195},
                        // {name: 'Somaliland', value: 9636.173},
                        // {name: 'Somalia', value: 9636.173},
                        // {name: 'Republic of Serbia', value: 3573.024},
                        // {name: 'Suriname', value: 524.96},
                        // {name: 'Slovakia', value: 5433.437},
                        // {name: 'Slovenia', value: 2054.232},
                        // {name: 'Sweden', value: 9382.297},
                        // {name: 'Swaziland', value: 1193.148},
                        // {name: 'Syria', value: 7830.534},
                        // {name: 'Chad', value: 11720.781},
                        // {name: 'Togo', value: 6306.014},
                        // {name: 'Thailand', value: 66402.316},
                        // {name: 'Tajikistan', value: 7627.326},
                        // {name: 'Turkmenistan', value: 5041.995},
                        // {name: 'East Timor', value: 10016.797},
                        // {name: 'Trinidad and Tobago', value: 1328.095},
                        // {name: 'Tunisia', value: 10631.83},
                        // {name: 'Turkey', value: 72137.546},
                        // {name: 'Tanzania', value: 44973.33},
                        // {name: 'Uganda', value: 33987.213},
                        // {name: 'Ukraine', value: 46050.22},
                        // {name: 'Uruguay', value: 3371.982},
                        // {name: 'United States', value:0},
                        // {name: 'Uzbekistan', value: 27769.27},
                        // {name: 'Venezuela', value: 236.299},
                        // {name: 'Vietnam', value: 89047.397},
                        // {name: 'Vanuatu', value: 236.299},
                        // {name: 'West Bank', value: 13.565},
                        // {name: 'Yemen', value: 22763.008},
                        // {name: 'South Africa', value: 51452.352},
                        // {name: 'Zambia', value: 13216.985},
                        // {name: 'Zimbabwe', value: 13076.978}
                    ]
                }
            ],
        };
        //中国地图参数
        this.chinaOption = {
            tooltip: {
                trigger: 'item',
                formatter: function (params) {
                    var html = '<div class="area-title">' + params.name + '</div>';
                    var data = params.data? params.data.data:''; //南沙群岛时，没有数据，直接返回
                    // try {
                    //    var data = params.data.data; //南沙群岛时，没有数据，直接返回
                    // }catch (e) {
                    //     return
                    // }
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
                axisPointer: {// 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'// 默认为直线，可选为：'line' | 'shadow'
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
                    name:bt.lang('bi.money.map.municipalities') ,//'所属市',
                    // data: ['广州市', '深圳市', '中山市', '东莞市', '惠州市', '河源市', '韶关市'],
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: bt.lang('table.fbsBudget.corpId')+'：'+$.trim($('#currencies-selected').text())

                }
            ],
            toolbox: {
                show: true,
                zlevel:0,                                   //所属图形的Canvas分层，zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面
                z:2,
                orient: 'horizontal',
                right: '20',
                top: '0px',
                feature: {
                    // mark: {show: true},
                    //dataView: {show: true, readOnly: false},
                    myTool1: {
                        show: true,
                        title: bt.lang('bi.money.map.switchToPie') ,//'切换为饼图',
                        icon: 'path://M1628.18667,390.077458 C1628.32328,390.057375 1628.4503,390.151833 1628.47038,390.288436 C1628.49046,390.425039 1628.396,390.552057 1628.2594,390.57214 C1624.96907,391.055875 1622.5,393.890846 1622.5,397.25 C1622.5,400.977922 1625.52208,404 1629.25,404 C1632.5884,404 1635.41161,401.56076 1635.91944,398.296239 C1635.94067,398.159808 1636.06847,398.066414 1636.2049,398.087638 C1636.34133,398.108861 1636.43472,398.236664 1636.4135,398.373094 C1635.86793,401.880252 1632.83579,404.5 1629.25,404.5 C1625.24594,404.5 1622,401.254064 1622,397.25 C1622,393.641915 1624.65179,390.597146 1628.18667,390.077458 Z M1629.25,389 C1633.80635,389 1637.5,392.693651 1637.5,397.25 C1637.5,397.388071 1637.38807,397.5 1637.25,397.5 L1637.25,397.5 L1629.25,397.5 C1629.11193,397.5 1629,397.388071 1629,397.25 L1629,397.25 L1629,389.25 C1629,389.111929 1629.11193,389 1629.25,389 Z M1629.5,389.503956 L1629.5,397 L1636.99604,397 L1636.9842,396.751128 C1636.73228,392.785731 1633.4982,389.630662 1629.5,389.503956 L1629.5,389.503956 Z'
                        ,onclick: function (){
                            map.createPieMap()
                        },
                        iconStyle:{
                            normal:{
                                color:'#fff',//设置颜色
                                borderColor:'#999',
                                borderWidth:1
                            }
                        }
                    },
                    magicType: {
                        show: true,
                        type: ['line', 'bar',],
                        title:{
                            line: bt.lang('bi.money.map.switchToLine') ,
                            bar: bt.lang('bi.money.map.switchToBar') ,
                        },
                        icon:{
                            line:'path://M1646.23333,390 C1646.3622,390 1646.46667,390.104467 1646.46667,390.233334 L1646.466,403.5 L1659.75,403.5 C1659.88807,403.5 1660,403.611929 1660,403.75 C1660,403.888071 1659.88807,404 1659.75,404 L1646.23333,404 L1646.23333,404 C1646.10447,404 1646,403.895533 1646,403.766666 L1646,390.233334 C1646,390.104467 1646.10447,390 1646.23333,390 Z M1655.32674,398.411503 L1659.59714,393.08544 C1659.6791,392.983212 1659.82349,392.970988 1659.91964,393.058136 C1659.99976,393.130759 1660.0211,393.249473 1659.97856,393.346187 L1659.94532,393.40103 L1655.52461,398.914561 C1655.45569,399.000517 1655.34279,399.02274 1655.25154,398.976119 L1655.19991,398.939843 L1650.79698,394.845129 L1647.3917,398.518791 C1647.30301,398.614467 1647.15817,398.615587 1647.06818,398.521293 C1646.9932,398.442715 1646.97997,398.322674 1647.02896,398.229475 L1647.06583,398.177323 L1650.62241,394.340433 C1650.69323,394.264035 1650.80048,394.247545 1650.887,394.293212 L1650.93596,394.328089 L1655.32674,398.411503 L1659.59714,393.08544 L1655.32674,398.411503 Z',
                            bar:'path://M1607.5,396 C1607.77614,396 1608,396.223858 1608,396.5 L1608,403.5 C1608,403.776142 1607.77614,404 1607.5,404 L1605.5,404 C1605.22386,404 1605,403.776142 1605,403.5 L1605,396.5 C1605,396.223858 1605.22386,396 1605.5,396 L1607.5,396 Z M1612.5,390 C1612.77614,390 1613,390.223858 1613,390.5 L1613,403.5 C1613,403.776142 1612.77614,404 1612.5,404 L1610.5,404 C1610.22386,404 1610,403.776142 1610,403.5 L1610,390.5 C1610,390.223858 1610.22386,390 1610.5,390 L1612.5,390 Z M1617.5,393 C1617.77614,393 1618,393.223858 1618,393.5 L1618,403.5 C1618,403.776142 1617.77614,404 1617.5,404 L1615.5,404 C1615.22386,404 1615,403.776142 1615,403.5 L1615,393.5 C1615,393.223858 1615.22386,393 1615.5,393 L1617.5,393 Z M1607.5,396.5 L1605.5,396.5 L1605.5,403.5 L1607.5,403.5 L1607.5,396.5 Z M1612.5,390.5 L1610.5,390.5 L1610.5,403.5 L1612.5,403.5 L1612.5,390.5 Z M1617.5,393.5 L1615.5,393.5 L1615.5,403.5 L1617.5,403.5 L1617.5,393.5 Z'
                        },
                        iconStyle:{
                            normal:{
                                color:'#fff',//设置颜色
                                borderColor:'#999',
                                borderWidth:1
                            }
                        }
                    },
                    // restore: {show: true},
                    saveAsImage: {
                        show: true,
                        title:bt.lang('bi.money.map.saveAsImage'),
                        icon:'path://M1599.67902,398.749966 C1599.79736,398.749966 1599.8965,398.832224 1599.92241,398.942675 L1599.92902,399 L1599.92902,403.02124 C1599.92902,403.139587 1599.84678,403.238727 1599.73634,403.264638 L1599.67902,403.27124 L1585.74547,403.27124 C1585.6285,403.27124 1585.53006,403.190845 1585.5029,403.081952 L1585.4955,403.025371 L1585.42895,399.004131 C1585.42677,398.866078 1585.53683,398.752315 1585.67489,398.750034 C1585.79322,398.748079 1585.8937,398.828663 1585.92143,398.938663 L1585.92898,398.995869 L1585.99137,402.77124 L1599.42902,402.77124 L1599.42902,399 C1599.42902,398.861929 1599.54094,398.749966 1599.67902,398.749966 Z M1592.54706,389.756603 L1592.60438,389.75 C1592.72272,389.75 1592.82187,389.832233 1592.84778,389.942677 L1592.85438,390 L1592.85403,400.643 L1596.66662,396.832112 C1596.74798,396.750753 1596.87147,396.737193 1596.96687,396.791432 L1597.02018,396.832112 C1597.10154,396.913471 1597.1151,397.036955 1597.06086,397.132356 L1597.02018,397.185665 L1592.80378,401.402066 C1592.72242,401.483425 1592.59893,401.496985 1592.50353,401.442745 L1592.45022,401.402066 L1588.18126,397.133098 C1588.08362,397.035467 1588.08362,396.877176 1588.18126,396.779545 C1588.26261,396.698186 1588.3861,396.684626 1588.4815,396.738865 L1588.53481,396.779545 L1592.35403,400.599 L1592.35438,390 C1592.35438,389.881653 1592.43661,389.782513 1592.54706,389.756603 Z'

                    },
                    // dataZoom:{
                    //     show:true
                    // },
                },
                iconStyle:{
                    normal:{
                        color:'#fff',//设置颜色
                        borderColor:'#9b9b9b',
                    }
                }


            },
            series: [
                {
                    name: bt.lang('table.accountDTO.balance')+ bt.lang('table.datatable.total'),//'余额合计',
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
                subtext: bt.lang('table.fbsBudget.corpId')+'：'+$.trim($('#currencies-selected').text()),//fubiaoti
                left:"48%",
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
                right: '20',
                top: '0px',
                feature: {
                    // mark: {show: true},
                    //dataView: {show: true, readOnly: false},
                    myTool1: {
                        show: true,
                        title:bt.lang('bi.money.map.switchToPie') ,//'切换为饼图',
                        icon: 'path://M1628.18667,390.077458 C1628.32328,390.057375 1628.4503,390.151833 1628.47038,390.288436 C1628.49046,390.425039 1628.396,390.552057 1628.2594,390.57214 C1624.96907,391.055875 1622.5,393.890846 1622.5,397.25 C1622.5,400.977922 1625.52208,404 1629.25,404 C1632.5884,404 1635.41161,401.56076 1635.91944,398.296239 C1635.94067,398.159808 1636.06847,398.066414 1636.2049,398.087638 C1636.34133,398.108861 1636.43472,398.236664 1636.4135,398.373094 C1635.86793,401.880252 1632.83579,404.5 1629.25,404.5 C1625.24594,404.5 1622,401.254064 1622,397.25 C1622,393.641915 1624.65179,390.597146 1628.18667,390.077458 Z M1629.25,389 C1633.80635,389 1637.5,392.693651 1637.5,397.25 C1637.5,397.388071 1637.38807,397.5 1637.25,397.5 L1637.25,397.5 L1629.25,397.5 C1629.11193,397.5 1629,397.388071 1629,397.25 L1629,397.25 L1629,389.25 C1629,389.111929 1629.11193,389 1629.25,389 Z M1629.5,389.503956 L1629.5,397 L1636.99604,397 L1636.9842,396.751128 C1636.73228,392.785731 1633.4982,389.630662 1629.5,389.503956 L1629.5,389.503956 Z',
                        onclick: function (){
                            map.createPieMap()
                        },
                        iconStyle:{
                            normal:{
                                color:'#fff',//设置颜色
                                borderColor:'#999',
                                borderWidth:1
                            }
                        }
                    },
                    myTool2: {
                        show: true,
                        title:bt.lang('bi.money.map.switchToLine') ,//'切换为折线图',
                        icon:'path://M1646.23333,390 C1646.3622,390 1646.46667,390.104467 1646.46667,390.233334 L1646.466,403.5 L1659.75,403.5 C1659.88807,403.5 1660,403.611929 1660,403.75 C1660,403.888071 1659.88807,404 1659.75,404 L1646.23333,404 L1646.23333,404 C1646.10447,404 1646,403.895533 1646,403.766666 L1646,390.233334 C1646,390.104467 1646.10447,390 1646.23333,390 Z M1655.32674,398.411503 L1659.59714,393.08544 C1659.6791,392.983212 1659.82349,392.970988 1659.91964,393.058136 C1659.99976,393.130759 1660.0211,393.249473 1659.97856,393.346187 L1659.94532,393.40103 L1655.52461,398.914561 C1655.45569,399.000517 1655.34279,399.02274 1655.25154,398.976119 L1655.19991,398.939843 L1650.79698,394.845129 L1647.3917,398.518791 C1647.30301,398.614467 1647.15817,398.615587 1647.06818,398.521293 C1646.9932,398.442715 1646.97997,398.322674 1647.02896,398.229475 L1647.06583,398.177323 L1650.62241,394.340433 C1650.69323,394.264035 1650.80048,394.247545 1650.887,394.293212 L1650.93596,394.328089 L1655.32674,398.411503 L1659.59714,393.08544 L1655.32674,398.411503 Z',
                        onclick: function (param){
                            map.createProvinceMap(false,param);
                        },
                        iconStyle:{
                            normal:{
                                color:'#fff',//设置颜色
                                borderColor:'#999',
                                borderWidth:1
                            }
                        }
                    },
                    myTool3: {
                        show: true,
                        title: bt.lang('bi.money.map.switchToBar'),//'切换为柱状图',
                        icon:'path://M1607.5,396 C1607.77614,396 1608,396.223858 1608,396.5 L1608,403.5 C1608,403.776142 1607.77614,404 1607.5,404 L1605.5,404 C1605.22386,404 1605,403.776142 1605,403.5 L1605,396.5 C1605,396.223858 1605.22386,396 1605.5,396 L1607.5,396 Z M1612.5,390 C1612.77614,390 1613,390.223858 1613,390.5 L1613,403.5 C1613,403.776142 1612.77614,404 1612.5,404 L1610.5,404 C1610.22386,404 1610,403.776142 1610,403.5 L1610,390.5 C1610,390.223858 1610.22386,390 1610.5,390 L1612.5,390 Z M1617.5,393 C1617.77614,393 1618,393.223858 1618,393.5 L1618,403.5 C1618,403.776142 1617.77614,404 1617.5,404 L1615.5,404 C1615.22386,404 1615,403.776142 1615,403.5 L1615,393.5 C1615,393.223858 1615.22386,393 1615.5,393 L1617.5,393 Z M1607.5,396.5 L1605.5,396.5 L1605.5,403.5 L1607.5,403.5 L1607.5,396.5 Z M1612.5,390.5 L1610.5,390.5 L1610.5,403.5 L1612.5,403.5 L1612.5,390.5 Z M1617.5,393.5 L1615.5,393.5 L1615.5,403.5 L1617.5,403.5 L1617.5,393.5 Z',
                        onclick: function (param){
                            map.createProvinceMap(true,param);
                        },
                        iconStyle:{
                            normal:{
                                color:'#fff',//设置颜色
                                borderColor:'#999',
                                borderWidth:1
                            }
                        }
                    },
                    // magicType: {show: true, type: ['line', 'bar','pie']},
                    // restore: {show: true},
                    saveAsImage: {
                        show: true,
                        title:bt.lang('bi.money.map.saveAsImage'),
                        icon:'path://M1599.67902,398.749966 C1599.79736,398.749966 1599.8965,398.832224 1599.92241,398.942675 L1599.92902,399 L1599.92902,403.02124 C1599.92902,403.139587 1599.84678,403.238727 1599.73634,403.264638 L1599.67902,403.27124 L1585.74547,403.27124 C1585.6285,403.27124 1585.53006,403.190845 1585.5029,403.081952 L1585.4955,403.025371 L1585.42895,399.004131 C1585.42677,398.866078 1585.53683,398.752315 1585.67489,398.750034 C1585.79322,398.748079 1585.8937,398.828663 1585.92143,398.938663 L1585.92898,398.995869 L1585.99137,402.77124 L1599.42902,402.77124 L1599.42902,399 C1599.42902,398.861929 1599.54094,398.749966 1599.67902,398.749966 Z M1592.54706,389.756603 L1592.60438,389.75 C1592.72272,389.75 1592.82187,389.832233 1592.84778,389.942677 L1592.85438,390 L1592.85403,400.643 L1596.66662,396.832112 C1596.74798,396.750753 1596.87147,396.737193 1596.96687,396.791432 L1597.02018,396.832112 C1597.10154,396.913471 1597.1151,397.036955 1597.06086,397.132356 L1597.02018,397.185665 L1592.80378,401.402066 C1592.72242,401.483425 1592.59893,401.496985 1592.50353,401.442745 L1592.45022,401.402066 L1588.18126,397.133098 C1588.08362,397.035467 1588.08362,396.877176 1588.18126,396.779545 C1588.26261,396.698186 1588.3861,396.684626 1588.4815,396.738865 L1588.53481,396.779545 L1592.35403,400.599 L1592.35438,390 C1592.35438,389.881653 1592.43661,389.782513 1592.54706,389.756603 Z'
                        ,iconStyle:{
                            normal:{
                                color:'#fff',//设置颜色
                                borderColor:'#999',
                                borderWidth:1
                            }
                        }
                    },
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
                    name: bt.lang('table.accountDTO.balance')+ bt.lang('table.datatable.total'),//'余额合计',
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
        };
        // 省份数据存贮
        this.provinceData = null;
        this.dictsContinentData = null;
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
        this.setDictsContinentData();//获取大洲字典
        this.continentOptions[bt.lang('bi.money.map.southAmerica')] = {}
        this.continentOptions[bt.lang('bi.money.map.northAmerica')] = {}
        this.continentOptions[bt.lang('bi.money.map.europe')] = {}
        this.continentOptions[bt.lang('bi.money.map.otherPartsOfAsia')] = {}
        this.continentOptions[bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea')] = {}
        this.continentOptions[bt.lang('bi.money.map.africa')] = {}
        this.continentOptions[bt.lang('bi.money.map.oceania')] = {}
        this.setNameMapForWorldMap();
        this.createContinentOptions();
        this.creatSelectDicts();//元千万亿字典
        return that
    }
    //获取大洲字典
    Map.fn.setDictsContinentData = function(){
        var that = this;
        $.ajax({
            type: "GET",
            url: basePath+"/sys/dict/listByGroupCode?code=common.continent",
            dataType: 'json',
            async: false,
            success: function (result) {
                var data;
                if (result === null || !result) {
                    data = [];
                }else {
                    data = result;
                }

                that.dictsContinentData = data;
            }
        });
    }

    //设置大洲数据
    Map.fn.setContinentCountrysData = function(continent,moneyUnit){

        var that = this, continentCode;
        if (this.dictsContinentData){
            bt.each(this.dictsContinentData,function (i,item) {
                if (item.name == continent){
                    continentCode = item.code;
                }
            })
        }

        $.ajax({
            type: "post",
            url: basePath + "/bi/synthesize/moneyMap/getContinentCountryMapData",
            async: false, //同步执行
            data: {
                "continent": continentCode,
                "countryNameLanguage": "en",
                "moneyUnit": moneyUnit
            },
            dataType: "json", //返回数据形式为json
            success: function (result) {
                that.continentOptions[continent].series[0].data = JSON.parse(result.data);
            }
        });
    }

    //设置世界地图的块
    Map.fn.setNameMapForWorldMap= function(){
        this.option.series[0].nameMap = $.extend(true, {}, this.continentCountrys);
        this.option.series[0].nameMap.selectedMode = 'multiple';
    }
    //创建大洲地图信息
    Map.fn.createContinentOptions = function(){
        var that = this;
        bt.each(this.continentMapArr,function (index,item) {
            that.continentOptions[item.name] = $.extend(true, {}, that.modelCommonOption);
            $.extend(that.continentOptions[item.name].series[0],{
                name:item.name +bt.lang('bi.money.map.map'),
                mapType: item.map
            })
            if (item.name == bt.lang('bi.money.map.oceania')){
                $.extend(that.continentOptions[item.name].series[0],{
                    zoom:2,
                    boundingCoords: [
                        // 定位左上角经纬度
                        [-20, 50],
                        // 定位右下角经纬度
                        [340, -130]
                    ],

                })
            }else if (item.name == bt.lang('bi.money.map.europe')){
                that.continentOptions[item.name].series[0].zoom = 2;
            }else if (item.name == bt.lang('bi.money.map.africa')){
                that.continentOptions[item.name].series[0].zoom = 0.6;
            }else if (item.name == bt.lang('bi.money.map.southAmerica') ){
                $.extend(that.continentOptions[item.name].series[0],{
                    zoom:2,
                    boundingCoords: [
                        // 定位左上角经纬度
                        [-210,70],
                        // 定位右下角经纬度
                        [150, -110]
                    ],
                })
            }else if (item.name == bt.lang('bi.money.map.southeastAsiaAndJapanAndKorea') ){
                that.continentOptions[item.name].series[0].zoom = 0.9;
            }
            that.continentOptions[item.name].visualMap.color = [item.color,item.color,item.color];
        })

    }
    //初始化
    Map.fn.render = function () {
        var that = this;
        that.onSelectChange();
        that.onMap();
        that.closeMap();
        that.onRefreshMap();
        that.updateWorldMapData();
        that.myChart.setOption(this.option);
        that.refreshDate();
    }
    //注册地图点击事件
    Map.fn.onMap = function () {
        var that = this;
        if(!that.myChart){
            that.myChart = echarts.init(document.getElementById('main'));
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz['myChart']) {
                window.biz['myChart'] = [];
            }
            window.biz['myChart']['world'] = that.myChart;

            window.addEventListener("resize", function (){
                that.myChart.resize();
            });
            that.myChart.on("click", function (param) { //点击事件
                // 只有中国才穿透
                var moneyUnit = $('#currencies').find('.layui-this').data('value');
                if (param.value == 5) {
                    $('#map-china').removeClass('hide');
                    that.updateChinaMapData(moneyUnit)
                    that.myChart.setOption(that.chinaOption,true);
                    $("#currencies").addClass("hide");//隐藏下拉选择币类
                    $('#map-header').removeClass('is-world');
                }

                if (param.seriesId.indexOf('资金地图') !== -1 && param.seriesId !='chinaMap' && param.value != 5){
                    that.setContinentCountrysData(param.name,moneyUnit)
                    that.myChart.setOption(that.continentOptions[param.name],true);
                    that.updateContinentTittle(param.name);
                    $('#map-header').removeClass('is-world');
                    $("#currencies").addClass("hide");//隐藏下拉选择币类
                }
                // 点击省份时执行代码
                if(param.seriesId =='chinaMap'){
                    that.provinceData = that.config.clickChinaMapCallback(param,that.myChart,moneyUnit);
                    if (that.provinceData.length){
                        that.updateProvinceMapData()
                        that.createProvinceMap(true,param)
                        that.setProvinceMapSelectPosition(true)
                        // that.createTable(provinceData);
                    } else {
                        layer.alert(param.name +bt.lang('bi.money.map.balance.zero')+' 0.00')
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
        $('#map-header .item-header-text').off('click').on('click',function() {
            var name = $(this).data('name');
            var moneyUnit = $('#currencies').find('.layui-this').data('value');
            if (name == 'china') {
                $('#main').removeClass('hide');
                $('#draw-province').addClass('hide');
               // that.updateChinaMapData(moneyUnit);
                that.myChart.setOption(that.chinaOption, true);
                $('#map-province').addClass('hide');
                $("#currencies").addClass("hide");//显示下拉选择币类
                $('#map-header').removeClass('is-world');
                $('#map-select-moneyType').addClass('hide');
                that.myChart.resize();
            } else if (name == 'world') {
                $('#main').removeClass('hide');
                $('#draw-province').addClass('hide');
                $('#map-china').addClass('hide');
                $('#map-province').addClass('hide');
                $("#currencies").removeClass("hide");//显示下拉选择币类
                $('#map-header').addClass('is-world');
               // that.updateWorldMapData(moneyUnit);
                $('#map-continent').addClass('hide');
                $('#map-select-moneyType').addClass('hide');
                that.myChart.setOption(that.option, true);
                that.myChart.resize();
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
            bt.loading.show();
            var moneyUnit = $('#currencies').find('.layui-this').data('value');
            if(!$('#map-china').hasClass('hide')){ //中国地图
                if(!$('#map-province').hasClass('hide')){ // 省份数据
                    var name = $('#province').text();
                    that.provinceData = that.config.clickChinaMapCallback({name:name},that.myChart,moneyUnit);
                    that.updateProvinceMapData();
                    that.createProvinceMap(true,{name:name});
                    //that.createTable(provinceData);
                }else { // 中国地图
                    that.updateChinaMapData();
                    that.myChart.setOption(that.chinaOption,true);
                }
            }else { //世界地图
                if (!$('#map-continent').hasClass('hide')){ //大洲地图
                    var name = $('#map-continent').find('.item-header-text').text().replace('地图','');
                    that.setContinentCountrysData(name)
                    that.myChart.setOption(that.continentOptions[name]);
                } else { //世界地图
                    that.updateWorldMapData();
                    that.myChart.setOption(that.option,true)
                }
            }
            that.refreshDate();
            bt.loading.hide();
        })
    }
    // 更新中国地图数据
    Map.fn.updateChinaMapData = function (moneyUnit) {
        var moneyUnit = $('#currencies').find('.layui-this').data('value');
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
    Map.fn.updateWorldMapData = function (moneyUnit) {
        var that = this;
        var moneyUnit = $('#currencies').find('.layui-this').data('value');
        moneyUnit = moneyUnit?moneyUnit:'yuan';
        $.ajax({
            type: "post",
            url: basePath + "/bi/synthesize/moneyMap/getWorldMapData",
            async: false, //同步执行
            data: {
                "moneyUnit": moneyUnit
            },
            dataType: "json", //返回数据形式为json
            success: function (result) {
                var data = JSON.parse(result.data);
                bt.each(data, function (i,item) {
                    item.name = bt.lang(item.name)
                })
                that.option.series[0].data = data;
                if(that.myChart){
                    that.myChart.hideLoading();
                }
            }
        });
    }
    // 更新省地图数据
    // 参数flag 为true 代表是通过单选框选择来刷新数据的，不用重组单选框;flag 为false 代表是切换了省份，单选框要重置内容
    Map.fn.updateProvinceMapData = function (moneyunit,flag) {
        var units =[],that = this;//units 为币别集合
        var moneyunit = moneyunit?moneyunit:bt.lang('table.curName.rmb');
        var province = this.provinceData,data = [],city = [],pieData = [],capitalByMoneyUnit = [];
        for(var i = 0; i <  province.length; i++ ){
            var capital = province[i].capital;
            if (capital && capital.length){
                for (var j = 0,len = capital.length; j <len; j++){
                    if (capital[j].currency == moneyunit){
                        var copy = Object.assign({}, capital[j]);
                        copy.name = province[i].name
                        capitalByMoneyUnit.push(copy)
                    }
                    if (units.indexOf(capital[j].currency) == -1){
                        units.push(capital[j].currency)
                    }
                }
            }
        }

        capitalByMoneyUnit.sort(function(a,b){
            return  b.value_currency -a.value_currency;
        });

        bt.each(capitalByMoneyUnit,function (i,capital) {
            data.push(capital.value_currency);
            city.push(capital.name);
            pieData.push({
                'name':capital.name,
                'value':capital.value_currency
            })
        })
        this.provinceOption.xAxis[0].data = city;
        this.provinceOption.series[0].data = data;
        this.provincePieOption.title.subtext = this.provinceOption.yAxis[0].name = bt.lang('table.fbsBudget.corpId')+'：'+$.trim($('#currencies-selected').text());
        this.provincePieOption.legend.data = city;
        this.provincePieOption.series[0].data = pieData;

        if (!flag){ //更新币别单选框
            var isHasCNY = units.indexOf(bt.lang('table.curName.rmb')) > -1;
            var html ='';
            if (isHasCNY){
                html += '<input type="radio" name="moneyType" value="'+bt.lang('table.curName.rmb')+'" title="'+bt.lang('table.curName.rmb')+'" checked="" lay-filter="map-select-moneyType">'
            }
            bt.each(units,function (i,v) {
                if (isHasCNY && v != bt.lang('table.curName.rmb') ){
                    html += '<input type="radio" name="moneyType" value="'+v+'" title="'+v+'" lay-filter="map-select-moneyType">'
                } else {
                    if (!isHasCNY){
                        if (i == 0){
                            html += '<input type="radio" name="moneyType" value="'+v+'" title="'+v+'" lay-filter="map-select-moneyType" checked="">'
                        } else {
                            html += '<input type="radio" name="moneyType" value="'+v+'" title="'+v+'" lay-filter="map-select-moneyType">'
                        }
                    }
                }
            })

            var moneyTypeContainer = $('#map-select-moneyType');
            moneyTypeContainer.html(html);
            form.render('radio');
            var marginLeft = -moneyTypeContainer.width()/2;
            moneyTypeContainer.css('margin-left',marginLeft+'px');
            form.on('radio(map-select-moneyType)', function(data){
                that.updateProvinceMapData(data.value,true);
                var isBar = $('#province2').hasClass('hide');
                if (isBar){ //柱状图或折线图
                    that.myProvinceChart.setOption({
                        xAxis:[
                            {data:that.provinceOption.xAxis[0].data}
                        ],
                        series:[
                            {data:that.provinceOption.series[0].data}
                        ]
                    })
                } else {　//　饼图
                    that.myPieChart.setOption(that.provincePieOption)
                }
            });
        }
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
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz['myChart']) {
                window.biz['myChart'] = [];
            }
            window.biz['myChart']['pieChart'] = this.myPieChart;
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
            if (!window.biz) {
                window.biz = [];
            }
            if (!window.biz['myChart']) {
                window.biz['myChart'] = [];
            }
            window.biz['myChart']['barChart'] = this.myProvinceChart;
        }
        if(isBar){
            this.provinceOption.series[0].type = 'bar';
        }else{
            this.provinceOption.series[0].type = 'line';
            this.provinceOption.xAxis[0].boundaryGap = false;
        }
        this.myProvinceChart.setOption(this.provinceOption,true);
        $('#map-province').removeClass('hide')
        $('#province').text(param.name)
    }

    Map.fn.refreshDate = function () {
        var date = bt.date.format(new Date,'yyyy-MM-dd hh:mm:ss');
        $('#mapDate').text(date)
    }

    //更新左侧大洲标题
    Map.fn.updateContinentTittle = function (name) {
        $('#map-continent').removeClass('hide')
            .find('.item-header-text').html(name+bt.lang('bi.money.map.map'))
            .data('name', name);
    }

    //设置省份地图的下拉选择位置，要求是居上居中
    Map.fn.setProvinceMapSelectPosition = function (flag) {
        if (flag){
            $('#map-select-moneyType').removeClass('hide')
        }else {
            $('#map-select-moneyType').addClass('hide')
        }
    }

    Map.fn.onSelectChange = function () {
        var that = this;
        // 货币单位下拉框选择回调
        element.on('nav(currencies)', function(elem){ //币种选择方法
            $("#currencies-selected").html($(elem).text());
            var value = elem.data('value');
            that.updateWorldMapData(value);
            that.myChart.setOption(that.option);
            elem.parent().removeClass('layui-show')

        });
    }

    Map.fn.creatSelectDicts = function () {
        var dictGroup = $('#currencies').data('group'),data=[],html='';
        $.ajax({
            type: "GET",
            url: basePath+"/sys/dict/listByGroupCode?code="+dictGroup,
            dataType: 'json',
            async: false,
            success: function (result) {
                if (result === null || !result) {
                    data = [];
                }else {
                    data = result;
                }

                bt.each(data, function (i,v) {
                    html += '<dd data-value="'+v.code+'"'
                    if(!i){
                        html += 'class="layui-this" >'
                        $('#currencies-selected').text(v.name)
                    }else {
                        html += '>'
                    }
                    html += '<a href="javascript:;" data-value="'+v.code+'">'+v.name+'</a></dd>'
                })
                $('#currencies-child').html(html);
                element.init();
            }
        });
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
    exports('capitalMap', obj);
}).addcss('../../../css/worldMap.css','worldMap')
