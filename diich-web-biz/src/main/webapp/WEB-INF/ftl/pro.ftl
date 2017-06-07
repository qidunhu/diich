<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>
    <#if (obj.contentFragmentList?size>0)>
       <#list obj.contentFragmentList as cf>
        <#if (obj.lang == "chi")>
            <#if cf.attributeId == 4>
                <#assign proname = cf.content>
            ${cf.content}
            </#if>
        </#if>
        <#if (obj.lang == "eng")>
            <#if cf.attributeId == 5>
                <#assign proname = cf.content>
            ${cf.content}
            </#if>
        </#if>
      </#list>
    </#if>
    </title>
    <link rel="shortcut icon" type="image/x-icon" href="assets/images/logo.png" media="screen" />
    <link rel="stylesheet" href="assets/css/common.css">
    <link rel="stylesheet" href="assets/css/layout.css">
    <script src="./data/keyword.js"></script>
    <script src="./assets/js/jquery.min.js"></script>
        <#if (obj.lang == "chi")>
            <script src="./assets/js/system.js"></script>
        </#if>
        <#if (obj.lang == "eng")>
            <script src="./assets/js/system_en.js"></script>
        </#if>

    <script src="./assets/js/html5media.min.js"></script>
    <script src="./js/category.js"></script>
    <script src="./js/citys.js"></script>
    <script src="./js/doT.js"></script>
    <script src="./js/serverinfo.js"></script>
    <script src="./js/masters.js"></script>
    <script src="./data/master_data.js"></script>
    <script src="./assets/js/inputs.js"></script>
    <script>
        $(function () {
            var btn=$('a[data-type="mediaLayer"]').on('click',function () {
                var type = $(this).attr('data-type');
                var index = parseInt($(this).attr('data-id'));
                detailCommon.mediaShow(type, index);
            })
        })
    </script>
</head>

<body>
<#assign caturi="http://diich.efeiyi.com" />
<div class="header header_detail"></div>
<!--//End header -->
<div class="filter_search filter_search_fixed">
    <div class="content">
        <form class="form" action="${caturi}/page/search.html">
            <input class="ipt" type="text" id="keyword" name="keyword" value="" autocomplete="off">
            <input type="hidden" id="area_code" name="area_code" value="" />
            <input type="hidden" id="gb_category_code" name="gb_category_code" value="" />
            <input type="hidden" id="type" name="type" value="" />
            <input class="submit" type="button" value="搜索" onclick="submit()">
            <div class="suggest" style="display: none;">
                <ul>

                </ul>
            </div>
        </form>
        <!--//End form-->

        <div class="attr">
            <span id="attr_text">所属类别</span>
            <span id="area_text">全球</span>
        </div>
        <!--//End attribute-->

        <div class="dropbox" id="drag">
            <!--//ENd 全部-->

            <div class="item" id="item_1">
                <dl class="level">
                    <dt>
                    <div class="title">一级分类</div>
                    <div class="subtitle">所有分类</div>
                    </dt>
                    <dd>
                        <ul id="mainCategory">

                        </ul>
                    </dd>
                </dl>
                <dl class="level2">
                    <dt>
                    <div class="title">二级分类</div>
                    <div class="subtitle">所有二级分类</div>
                    </dt>
                    <dd>
                        <ul id="catecontent">

                        </ul>
                    </dd>
                </dl>
            </div>
            <!--//End 所属分类-->

            <div class="item">
                <dl class="level">
                    <dt>
                    <div class="title">位置</div>
                    </dt>
                    <dd>
                        <ul>
                            <li>中国</li>
                        </ul>
                    </dd>
                </dl>
                <dl class="level2">
                    <dt>
                    <div class="title">按照字母顺序</div>
                    </dt>
                    <dd>
                        <ul id="citycontent">
                            <li>安微</li>
                            <li>澳门</li>
                            <li>北京</li>
                            <li>上海</li>
                            <li>福建</li>
                            <li>甘肃</li>
                            <li>广东</li>
                        </ul>
                    </dd>
                </dl>
            </div>
            <!--//End 位置-->

        </div>
        <!--//End attribute-->
    </div>
</div>
<!--//End filter_search -->
<#assign masterpage = "http://resource.efeiyi.com/html/master/"/>
<#assign workspage = "http://resource.efeiyi.com/html/works/"/>
<#assign prouri="../../image/project/" />
<#assign masteruri="../../image/master/" />
<div class="container">
    <div class="bd detail">
        <div class="mainbg">
        <#assign backImgUrl="assets/uploads/head.png">
        <#if (obj.contentFragmentList?size>0)>
            <#list obj.contentFragmentList as cf>
                <#if cf.attributeId == 1>
                    <#if (cf.resourceList?size>0)>
                        <#list cf.resourceList as res>
                            <#if res.type==0 && res.status==0>
                                <#assign backImgUrl="${prouri}${res.uri}">
                            </#if>
                        </#list>
                    </#if>
                </#if>
            </#list>
        </#if>
            <img src="${backImgUrl}" alt="">
        <#if (obj.contentFragmentList?size>0)>
            <#list obj.contentFragmentList as cf>
                <#if cf.attributeId == 1>
                    <#if (cf.resourceList?size>0)>
                        <#list cf.resourceList as res>
                            <#if res.type==1 && res.status==0>
                                <video poster="${backImgUrl}" src="${prouri}${res.uri}"> </video>
                                <span data-type="1"  class="play_big"> </span>
                            </#if>
                        </#list>
                    </#if>
                </#if>
            </#list>
        </#if>


        </div>
        <!--//End main-->
        <div class="crumbs">
            <span>
            <#if (obj.lang == "chi")>
                非遗名录
            </#if>
            <#if (obj.lang == "eng")>
                The heritage
            </#if>
            </span>
        <#--<i class="gt"></i>
        <span><a href="" title="口头传说和表述">口头传说和表述</a></span>-->
        <#if (obj.ichCategory.name)??>
            <i class="gt"></i>
            <span><a href="${caturi}/page/search.html?gb_category_code=${obj.ichCategory.gbCategory}" data-id="${obj.ichCategory.gbCategory}" title="${obj.ichCategory.name}"> ${obj.ichCategory.name}</a></span>
            <#if (obj.ichCategory.children??) && (obj.ichCategory.children?size>0)>
                <#list obj.ichCategory.children as ch>
                    <i class="gt"></i>
                    <span><a href="${caturi}/page/search.html?gb_category_code=${ch.gbCategory}" data-id="${ch.gbCategory}" title="${ch.name}"> ${ch.name}</a></span>
                    <#if (ch.children)?? && (ch.children?size>0)>
                        <#list ch.children as chh>
                            <i class="gt"></i>
                            <span><a href="${caturi}/page/search.html?gb_category_code=${chh.gbCategory}" data-id="${chh.gbCategory}" title="${chh.name}"> ${chh.name}</a></span>
                        </#list>
                    </#if>
                </#list>
            </#if>
        </#if>
            <i class="gt"></i>
            <span class="last"><#if (obj.contentFragmentList?size>0)>
                                        <#list obj.contentFragmentList as cf>
                                            <#if (obj.lang == "chi")>
                                                <#if cf.attributeId == 4>
                                                     ${cf.content}
                                                </#if>
                                            </#if>
                                            <#if (obj.lang == "eng")>
                                                <#if cf.attributeId == 5>
                                                     ${cf.content}
                                                </#if>
                                            </#if>
                                        </#list>
                                    </#if>
                </span>
        </div>
        <!--//End crumbs-->

        <div class="card">
            <div class="card_main">
                <div class="floor">
                    <a class="share" title="分享"></a>
                    <a class="praise active" title="点赞" style="position: relative;"></a>
                <#assign numPic = 0>
                <#assign numVed = 0>
                <#if (obj.contentFragmentList?size>0)>
                    <#list obj.contentFragmentList as cf>
                        <#if (cf.resourceList??) && (cf.resourceList?size>0)>
                            <#list cf.resourceList as res>
                                <#if res.type==0>
                                    <#assign numPic = numPic+1>
                                </#if>
                                <#if res.type==1>
                                    <#assign numVed = numVed +1>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <#if (numPic >0) && (numVed >0)>
                    <a class="album albums" onclick="show()" data-type="mediaLayer" datatype="0" data-id="0"><i class="play_sm"></i>
                        <#if (obj.lang == "chi")>
                            ${numPic}张图片/${numVed}个视频
                        </#if>
                        <#if (obj.lang == "eng")>
                          ${numPic} piece of images/${numVed} video
                        </#if>
                    </a>
                </#if>
                <#if (numPic >0) && (numVed =0)>
                    <a class="album albums"><i class="icon_img"></i>
                        <#if (obj.lang == "chi")>
                            ${numPic}张图片
                        </#if>
                        <#if (obj.lang == "eng")>
                            ${numPic} piece of images
                        </#if>
                    </a>
                </#if>
                <#if (numPic =0) && (numVed >0)>
                    <a class="album albums"><i class="icon_img"></i>
                        <#if (obj.lang == "chi")>
                              ${numVed}个视频
                        </#if>
                        <#if (obj.lang == "eng")>
                             ${numVed}  video
                        </#if>
                    </a>
                </#if>
                <#-- <a class="album" onclick="show()" data-type="mediaLayer"><i class="play_sm"></i>【视频】昆曲传承人讲述昆曲…(2个视频／9张图片)</a>-->
                    <div class="share_box">
                        <div class="icons">
                            <a href="" class="sina"></a>
                            <a href="" class="facebook"></a>
                            <a href="" class="twitter"></a>
                        </div>
                        <img class="qrcode" src="assets/images/code.png" alt="">
                    </div>
                </div>
                <!--//End -->
                <div class="detail_title">
                    <h2><#if (obj.contentFragmentList?size>0)>
                                <#list obj.contentFragmentList as cf>
                                    <#if obj.lang == "chi">
                                        <#if cf.attributeId == 4>
                                        ${cf.content}
                                        </#if>
                                    </#if>
                                    <#if obj.lang == "eng">
                                        <#if cf.attributeId == 5>
                                            ${cf.content}
                                        </#if>
                                    </#if>
                                </#list>
                         </#if>
                    </h2>
                    <div class="doi_code">
                        <i class="icon">ID</i>
                        <span>
                        <#if obj.lang == "chi">
                            标识码：
                        </#if>
                         <#if obj.lang == "eng">
                             Identification code：
                         </#if>
                            <#if (obj.contentFragmentList?size>0)>
                            <#list obj.contentFragmentList as cf>
                                <#if cf.attributeId == 2>
                                    ${cf.content}
                                </#if>
                            </#list>
                        </#if></span>
                        <em class="icon"></em>
                        <div class="drop">
                            <img src="assets/images/code.png" alt="">
                        </div>
                    </div>
                </div>
                <!--//End title-->

                <div class="bd subtxt">
                        <span>
                            <strong>
                                <#if obj.lang == "chi">
                                    类别：
                                </#if>
                                <#if obj.lang == "eng">
                                    category：
                                </#if>
                            </strong>
                            <em><#if (obj.ichCategory.name)??>
                            ${obj.ichCategory.name}
                                <#if ((obj.ichCategory.children)?? && obj.ichCategory.children?size>0)>
                                    <#list obj.ichCategory.children as ch>
                                        -${ch.name}
                                        <#if (ch.children)?? && (ch.children?size>0)>
                                            <#list ch.children as chh>
                                                -${chh.name}
                                            </#list>
                                        </#if>
                                    </#list>
                                </#if>
                            </#if>
                            <#--<#if type??>
                                                ${type.name}
                               </#if>-->
                            </em>
                        </span>
                    <span>

                    <#if (obj.contentFragmentList?size>0)>
                        <#list obj.contentFragmentList as cf>
                            <#if cf.attributeId == 33 && cf.content??>
                               <strong>
                                <#if obj.lang == "chi">
                                    地区：
                                </#if>
                                <#if obj.lang == "eng">
                                    district：
                                </#if>
                            </strong>
                                <#assign codeList = cf.content?split(";")>
                                <#list codeList as s>
                                    <em>${s}</em>
                                    <#if s_index+1 < (codeList?size)>
                                        <i>|</i>
                                    </#if>
                                </#list>
                            </#if>
                        </#list>
                    </#if>
                        </span>
                </div>
                <!--//End-->

            <#if obj.ichMasterList?? && (obj.ichMasterList?size > 0)>
                <div class="bd inheritor">

                    <div class="tname">
                        <#if obj.lang == "chi">
                            代表性传承人
                        </#if>
                        <#if obj.lang == "eng">
                            representativeness
                        </#if>
                    </div>
                    <div class="master">
                        <ul>
                        <li>
                            <#list obj.ichMasterList as master>
                                <#assign masterPic="assets/uploads/default_avatar2.png">
                                <#if master.contentFragmentList??>
                                    <div class="item">
                                        <#list master.contentFragmentList as cf>
                                            <#if cf.attributeId == 113 && cf.targetType == 1>

                                                <#if cf.resourceList??>
                                                    <#list cf.resourceList as r>
                                                        <#if r.uri??>
                                                            <#assign masterPic="${masteruri}${r.uri}">
                                                        </#if>
                                                    </#list>
                                                </#if>
                                            <#---->
                                            </#if>

                                        </#list>
                                        <a href="${masterpage}${master.id?c }.html" class="avatar">
                                            <img src="${masterPic}" alt=""/>
                                        </a>

                                        <span class="txt">
                                            <#list master.contentFragmentList as cf>
                                                <#if obj.lang == "chi">
                                                    <#if cf.attributeId == 13 && cf.targetType == 1>
                                                        <p class="name"><a href="${masterpage}${master.id?c }.html">${cf.content}</a></p>
                                                    </#if>
                                                </#if>
                                                <#if obj.lang == "eng">
                                                    <#if cf.attributeId == 14 && cf.targetType == 1>
                                                        <p class="name"><a href="${masterpage}${master.id?c }.html">${cf.content}</a></p>
                                                    </#if>
                                                </#if>
                                                <#if cf.attributeId == 50 && cf.targetType == 1>
                                                    <p >${cf.content}</p>
                                                </#if>

                                                <#if cf.attributeId == 48 && cf.targetType == 1>
                                                    <p >${cf.content}</p>
                                                </#if>
                                            </#list>

                                                    </span>
                                    </div>

                                </#if>
                                <#if ((master_index+1) %12 == 0) && (obj.ichMasterList?size > master_index+1)>
                                </li><li>
                                </#if>
                            </#list>

                        </li>

                        </ul>
                        <div class="more">
                            <a href="javascript:;"><span>
                                <#if obj.lang == "chi">
                                    其他<em></em>人
                                </#if>
                                <#if obj.lang == "eng">
                                    other<em></em>persons
                                </#if>
                            </span><i class="gt_big"></i></a>
                        </div>
                        <div class="prev"></div>
                        <div class="next"></div>
                        <div class="page"></div>
                    </div>
                </div>
            </#if>
                <!--//ENd-->
                <div class="bd batch">
                    <div class="tname">
                    <#if obj.lang == "chi">
                        非遗在中国
                        <i></i>
                    </#if>
                    <#if obj.lang == "eng">
                        The heritage in China
                        <i></i>
                    </#if>
                </div>
                    <div class="subcon">
                    <#if (obj.contentFragmentList?size>0)>
                        <#list obj.contentFragmentList as cf>
                            <#if cf.attributeId == 106 && cf.content??>
                                <span>
                                     <#if obj.lang == "chi">
                                         人类非物质文化遗产编号：
                                     </#if>
                                    <#if obj.lang == "eng">
                                        Human intangible cultural heritage number：
                                    </#if>
                                   ${cf.content}</span>
                            </#if>
                        </#list>
                    </#if>
                    <#if (obj.contentFragmentList?size>0)>
                        <#list obj.contentFragmentList as cf>
                            <#if cf.attributeId == 41 &&  cf.content??>
                                <span>
                                     <#if obj.lang == "chi">
                                         级别
                                     </#if>
                                    <#if obj.lang == "eng">
                                        rank
                                    </#if>
                                    ： ${cf.content} </span>
                            </#if>
                        </#list>
                    </#if>

                    <#if (obj.contentFragmentList?size>0)>
                        <#list obj.contentFragmentList as cf>
                            <#if cf.attributeId == 7 && cf.content??>
                                <span>
                                    <#if obj.lang == "chi">
                                        批次
                                    </#if>
                                    <#if obj.lang == "eng">
                                        batch
                                    </#if>
                                    ： ${cf.content} </span>
                            </#if>
                        </#list>
                    </#if>

                    </div>
                </div>
                <!--//ENd-->
            </div>
            <!--//End 主内容-->

            <div class="card_base">
                <duv class="detail_title">
                    <h2>
                        <#if obj.lang == "chi">
                            基礎信息
                        </#if>
                        <#if obj.lang == "eng">
                            Basic information
                        </#if>
                    </h2>
                </duv>
                <div class="info">
                    <ul>
                    <#if (obj.contentFragmentList?size>0)>
                        <#list obj.contentFragmentList as cf>
                            <#if cf.attribute?? && cf.attribute.dataType==0 && cf.content?? && cf.attributeId != 106 && cf.attributeId != 7 && cf.attributeId != 8>
                                <li>
                                    <span class="key">
                                        <#if obj.lang == "chi">
                                            ${cf.attribute.cnName}
                                        </#if>
                                        <#if obj.lang == "eng">
                                             ${cf.attribute.enName}
                                        </#if>
                                        ：</span>
                                    <span class="value">${cf.content}</span>
                                </li>
                            </#if>
                        </#list>
                    </#if>

                    </ul>
                </div>
            </div>
            <!--//End 基本信息-->
        </div>
    <#assign odd_even =0 />
    <#if obj.worksList?? && (obj.worksList?size>0)>
        <section class="bd floor odd">
            <div class="card">
                <header><h4>
                    <#if obj.lang == "chi">
                        代表作品
                    </#if>
                    <#if obj.lang == "eng">
                        Representative works
                    </#if>
                </h4><em>
                    <#if obj.lang == "chi">
                        共
                    </#if>
                    <#if obj.lang == "eng">
                        total
                    </#if>
                         ${obj.worksList?size}
                    <#if obj.lang == "chi">
                        项
                    </#if>
                    <#if obj.lang == "eng">
                        item
                    </#if>
                   </em></header>
                <article class="product_list">
                    <ul>
                        <#list obj.worksList as work>
                            <#if  work.contentFragmentList??>
                                <li>
                                    <!-- 保证图片在上面-->
                                    <#list work.contentFragmentList as c>
                                        <#if c.attributeId==25>
                                            <#if c.resourceList??>
                                                <#list c.resourceList as p>
                                                    <a href="${workspage}${work.id?c}.html"><img src="${p.uri}" alt=""></a>
                                                </#list>
                                            </#if>
                                        </#if>
                                    </#list>

                                    <#list work.contentFragmentList as c>
                                        <#if obj.lang == "chi">
                                            <#if c.attributeId==28>
                                            <p class="name">${c.content} </p
                                            </#if>
                                        </#if>
                                        <#if obj.lang == "eng">
                                            <#if c.attributeId==29>
                                                <p class="name">${c.content} </p
                                            </#if>
                                        </#if>

                                    </#list>
                                    <#list work.contentFragmentList as c>
                                        <#if c.attributeId==25>
                                            <#if c.resourceList??>
                                                <#list c.resourceList as p>
                                                    <p class="master"><#if p.description??>${p.description}</#if></p>
                                                </#list>
                                            </#if>
                                        </#if>
                                    <#-- <#if c.attributeId==31>
                                         <p class="master">${c.content}</p>
                                     </#if>-->
                                    </#list>
                                </li>
                            </#if>
                        </#list>
                        <#assign odd_even = odd_even+1 />
                    </ul>
                    <div class="page"></div>
                </article>
            </div>
        </section>
    </#if>


    <#if (obj.contentFragmentList?size>0)>
        <#list obj.contentFragmentList as cf>
            <#if (cf.attribute.dataType == 5 && cf.resourceList?? && cf.resourceList?size>0)>
                <section name="tuwen" class="bd floor <#if odd_even%2 == 0 >odd</#if><#if odd_even%2 != 0 >even</#if>">
                    <div class="card">
                        <header><h4>
                            <#if obj.lang == "chi">
                                 ${cf.attribute.cnName}
                            </#if>
                            <#if obj.lang == "eng">
                                 ${cf.attribute.enName}
                            </#if>
                        </h4></header>
                        <article class="text_img">
                            <div class="side">
                                <div class="item">
                                    <p>${cf.content?replace("\n","</p><p>")}</p>
                                </div>
                            </div>
                            <div class="media">
                                <ul>
                                    <#list cf.resourceList as r>
                                        <li>
                                            <#if r.type ==0>
                                                <img src="${prouri}${r.uri}" alt="">
                                                <#if r.description??>
                                                    <span>${r.description}</span>
                                                </#if>
                                            </#if>

                                            <#if r.type ==1>
                                                <div class="card_video">
                                                    <div class="time">30:24</div>
                                                    <div class="play" data-type="1" data-id="1" ></div>
                                                    <video poster="assets/uploads/exp2.png"  src="${prouri}${r.uri}" type="video/mp4" style="width: 100%;">

                                                    </video>
                                                </div>
                                                <#if r.description??>
                                                    <span>${r.description}</span>
                                                </#if>
                                            </#if>
                                            <#if (r_index == 1)>
                                                <#break />
                                            </#if>
                                        </li>
                                    </#list>
                                </ul>

                                <#if (cf.resourceList?size > 2) >
                                    <div class="more">
                                        <a class="albums" data-type="mediaLayer" datatype="0" data-id="0" href="javascript:;">
                                            <#if obj.lang == "chi">
                                                查看完整图集
                                            </#if>
                                            <#if obj.lang == "eng">
                                                View the complete set of images
                                            </#if>
                                            <i class="arrow_right"></i></a>
                                    </div>
                                </#if>
                            </div>
                        </article>
                    </div>
                </section>

                <#assign odd_even = odd_even+1 />
            </#if>
            <#if ((cf.attribute.dataType == 5 || cf.attribute.dataType == 1) && (!cf.resourceList?? || cf.resourceList?size==0))>

                <section class="bd floor <#if odd_even%2 == 0 >odd</#if><#if odd_even%2 != 0 >even</#if>">
                    <div class="card">
                        <header><h4>
                            <#if obj.lang == "chi">
                                ${cf.attribute.cnName}
                            </#if>
                            <#if obj.lang == "eng">
                                ${cf.attribute.enName}
                            </#if>
                        </h4></header>
                        <article class="plain_text">
                            <p>
                            ${cf.content?replace("\n", "</p><p>")}
                            </p>
                        <#--${cf.content?replace("\n","</p></p>")}-->

                        </article>
                    </div>
                </section>

                <#assign odd_even = odd_even+1 />
            </#if>

        </#list>
    </#if>

        <!--//ENd-->
    </div>
    <!--//End detail -->
</div>



<div class="bd footer"></div>
<!--//End--footer-->

<div class="side_fixed">
    <ul></ul>
    <a class="gotop" href="javascript:void(0)" title="返回顶部"></a>
</div>
<!--//End 右侧悬浮-->


<div class="media_layer" style="display:none;">
    <div class="content">
        <div class="head">
            <div class="menu">
                <span class="active">
                     <#if obj.lang == "chi">
                         图片
                     </#if>
                     <#if obj.lang == "eng">
                         picture
                     </#if>
                </span>

            <#if (obj.contentFragmentList?size>0)>
                <#assign breaklop=0>
                <#list obj.contentFragmentList as cf>
                    <#if breaklop==1><#break ></#if>
                        <#if cf.resourceList??>
                            <#list cf.resourceList as r>
                                <#if r.type ==1>
                                    <span>
                                        <#if obj.lang == "chi">
                                            视频
                                        </#if>
                                        <#if obj.lang == "eng">
                                            video
                                        </#if>
                                    </span>
                                    <#assign breaklop=1>
                                    <#break>
                                </#if>
                            </#list>
                        </#if>
                </#list>
            </#if>

            </div>
            <a href="" class="icon_close"></a>
        </div>
        <!--//End-->

        <div class="items album" style="display: block">
            <div class="title">
                <ul class="dt">


                <#if (obj.contentFragmentList?size>0)>
                    <#list obj.contentFragmentList as cf>
                        <#if cf.resourceList??>
                            <#list cf.resourceList as r>
                                <#if r.type ==0>
                                    <#if r.description??>
                                    <li>${r.description}</li>
                                    </#if>
                                </#if>
                            </#list>
                        </#if>
                    </#list>
                </#if>
                <#--   <li>《1111-名称示意最多显示20字…》</li>
                   <li>《2222-名称示意最多显示20字…》</li>
                   <li>《3333-名称示意最多显示20字…》</li>
                   <li>《4444-名称示意最多显示20字…》</li>
                   <li>《5555-名称示意最多显示20字…》</li>
                   <li>《6666-名称示意最多显示20字…》</li>-->
                </ul>
                <a href="" class="more">
                    <#if obj.lang == "chi">
                        查看详情
                    </#if>
                    <#if obj.lang == "eng">
                        Check the details
                    </#if>
                </a>
                <div class="master">
                    <div class="item">
                        <a class="avatar" href="">
                            <#if (obj.contentFragmentList?size>0)>
                                <#list obj.contentFragmentList as cf>
                                    <#if cf.attributeId == 1>
                                        <#if (cf.resourceList??) &&(cf.resourceList?size>0)>
                                            <#list cf.resourceList as res>
                                                <#if res.type==0 && res.status==0>
                                                    <img src="<#if res.uri??>${prouri}${res.uri}</#if>" alt="" width="94" height="70">
                                                </#if>
                                            </#list>
                                        </#if>
                                    </#if>
                                </#list>
                            </#if>
                        </a>
                        <span>
                            <#if (obj.contentFragmentList?size>0)>
                                <#list obj.contentFragmentList as cf>
                                    <#if obj.lang == "chi">
                                        <#if cf.attributeId == 4>
                                             ${cf.content}
                                        </#if>
                                    </#if>
                                    <#if obj.lang == "eng">
                                        <#if cf.attributeId == 5>
                                            ${cf.content}
                                        </#if>
                                    </#if>
                                </#list>
                            </#if>
                        </span>
                        <span class="auth">
                            <#if obj.lang == "chi">
                                UNESCO认证非遗项目
                            </#if>
                            <#if obj.lang == "eng">
                                UNESCO certification of non-legacy projects
                            </#if>
                        </span>
                    </div>
                </div>
            </div>
            <!--//End-->
            <div class="main">
                <ul class="media" id="imgs">

                <#if (obj.contentFragmentList?size>0)>
                    <#assign idx=0 />
                    <#list obj.contentFragmentList as cf>
                        <#if cf.resourceList??>
                        <#list cf.resourceList as r>
                            <#if r.type ==0>
                                <li><a href=""><img src="${prouri}${r.uri}" alt="" data-type="0" data-id="${idx}"></a></li>
                                <#assign idx=idx+1 />
                            </#if>
                        </#list>
                        </#if>
                    </#list>
                </#if>

                <#--
                                        <li><a href=""><img src="assets/uploads/media_02.jpg" alt=""></a></li>
                                        <li><a href=""><img src="assets/uploads/media_03.jpg" alt=""></a></li>
                                        <li><a href=""><img src="assets/uploads/media_04.jpg" alt=""></a></li>
                                        <li><a href=""><img src="assets/uploads/media_05.jpg" alt=""></a></li>
                                        <li><a href=""><img src="assets/uploads/media_06.jpg" alt=""></a></li>-->
                </ul>
                <ul class="num">
                    <li class="active a-active"></li>
                    <li class="line">/</li>
                    <li class="total"></li>
                </ul>
            </div>
            <!--//End-->
            <span class="prev"></span>
            <span class="next"></span>
        </div>
        <!--//End 相册-->

        <div class="items video">
            <div class="title">
                <ul class="dt">

                <#if (obj.contentFragmentList?size>0)>
                    <#list obj.contentFragmentList as cf>
                        <#if cf.resourceList??>
                        <#list cf.resourceList as r>
                            <#if r.type == 1>
                                <li>${r.description}</li>
                            </#if>
                        </#list>
                        </#if>
                    </#list>
                </#if>
                <#-- <li>111111</li>
                 <li>222222</li>
                 <li>333333</li>
                 <li>444444</li>
                 <li>555555</li>-->
                </ul>
            </div>
            <div class="main">
                <ul class="media" id="videos">

                <#if (obj.contentFragmentList?size>0)>
                    <#assign idx=0 />
                    <#list obj.contentFragmentList as cf>
                        <#if cf.resourceList??>
                        <#list cf.resourceList as r>
                            <#if r.type ==1>
                                <li><video src="${prouri}${r.uri}" controls  data-type="1" data-id="${idx}"></video></li>
                                <#assign idx=idx+1 />
                            </#if>
                        </#list>
                    </#if>
                    </#list>
                </#if>

                <#--   <li><video src="assets/uploads/video1.mp4" controls></video></li>
                   <li>
                       <video src="assets/uploads/video1.mp4" controls></video>
                   </li>
                   <li>
                       <video src="assets/uploads/video1.mp4" controls></video>
                   </li>
                   <li>
                       <video src="assets/uploads/video1.mp4" controls></video>
                   </li>
                   <li>
                       <video src="assets/uploads/video1.mp4" controls></video>
                   </li>-->
                </ul>
            <#-- <ul class="num">
                 <li class="active v-active">01_<span>鼓罗</span>(30:20)</li>
                 <li>02_<span>建国后昆剧杰出的英才</span>(60:00)</li>
                 <li>03_<span>水调歌头</span>(90:30)</li>
                 <li>04_<span>吕布与貂蝉</span>(45:15)</li>
                 <li>05_<span>三国演义</span>(45:15)</li>
             </ul>-->
            </div>
            <!--//End-->

            <span class="prev"></span>
            <span class="next"></span>
        </div>
        <!--//End 视频-->

    </div>
</div>


</body>
<script>
    $(function() {
        //初始化
        projectPage.init();

        //reset 视频 data-id
        $("#imgs img").each(function(){
            //alert($(this).attr("src")+"---"+$(this).attr("data-id"));
            var obj=$(this);
            if($(".mainbg img")){
                if($(this).attr("src")==$(".mainbg img").attr("src")){
                    $(".mainbg img").attr("data-id",$(this).attr("data-id"));
                    $(".mainbg img").attr("data-type",$(this).attr("data-type"));
                }

            };
            //section 代码块寻找
            $("section[name='tuwen']").each(function(index,item){
                $(this).find("img").each(function(){
                    if(obj.attr("src")==$(this).attr("src")){
                        $(this).attr("data-id",obj.attr("data-id"));
                        $(this).attr("data-type",obj.attr("data-type"));
                    }
                });
            });
        });
        //reset 视频 data-id
        $("#videos video").each(function(){
            //alert($(this).attr("src")+"---"+$(this).attr("data-id"));
            var obj=$(this);
            if($(".mainbg video")){
                if($(this).attr("src")==$(".mainbg video").attr("src")){
                    $(".mainbg video").attr("data-id",$(this).attr("data-id"));
                    $(".mainbg video").attr("data-type",$(this).attr("data-type"));
                }
            };
            //section 代码块寻找

            $("section[name='tuwen']").each(function(index,item){
                $(this).find("video").each(function(){
                    if(obj.attr("src")==$(this).attr("src")){

                        $(this).attr("data-id",obj.attr("data-id"));
                        $(this).attr("data-type",obj.attr("data-type"));
                    }
                });
            });
        });

        //控制header中英文显示
        <#if obj.version?? && (obj.version.chiId??) && (obj.version.engId??)>
            $(".language").show();
            <#if obj.lang == "eng">
                $(".en").addClass("active");
                $(".zh").removeClass("active");
                $(".zh").attr('href',${obj.version.chiId} + ".html");
            </#if>
            <#if obj.lang == "chi">
                $(".zh").addClass("active");
                $(".en").removeClass("active");
                $(".en").attr('href',${obj.version.engId} + ".html");
            </#if>
        </#if>
        <#if !obj.version?? || (!obj.version.chiId??) || (!obj.version.engId??)>
            $(".language").hide();
        </#if>




        var mainCategory = $('#mainCategory');

        //初始化分类数据
        $.each(category_all, function(index, content) {
            mainCategory.append("<li data-id=\"" + content.gbCategory + "\" >" + content.name + "</li>");
        });

        mainCategory.find('li').on('click', function() {
            $("#attr_text").text($(this).html());
            $("#gb_category_code").val($(this).attr("data-id"));
            $("#item_1").hide();

            //searchData_();
        });



        var searchPage = {
            init: function() {
                //$('.header_detail .content .info li.search').hide();
                $('.header_detail .content .info li.login').addClass('line');
                this.filterBar();
                this.search();
            },
            filterBar: function() {
                var obj = $('.filter_bar');
                var linkTab = obj.find('a');
                var iconTab = obj.find('.icon_tab');
                var proColumn = $('.pro_column3'); //搜索列表

                //筛选
                linkTab.on('click', function() {
                    $(this).addClass('active').siblings('a').removeClass('active');

                    //刷新搜索结果页

                    if ($(this).index() == 0) {
                        $("#type").val("");
                    }
                    if ($(this).index() == 1) {
                        $("#type").val("0");
                    }
                    if ($(this).index() == 2) {
                        $("#type").val("1");
                    }
                    if ($(this).index() == 3) {
                        $("#type").val("2");
                    }

                    searchData_();

                    return false;
                });

                //切换图标
                iconTab.on('click', function() {
                    if ($(this).hasClass('active')) { //九宫格
                        $(this).removeClass('active');
                        proColumn.removeClass('active');
                    } else { //横排
                        $(this).addClass('active');
                        proColumn.addClass('active');
                    }
                });
            },
            search: function() {
                var filter = $('.filter_search'); //下拉搜索
                var filterFixed = $('.filter_search_fixed');
                var ipt = filter.find('.ipt');
                var iptVal = ipt.val();
                var filterAll = filter.find('.attr span'); //筛选项
                var filterItem = filter.find('.item'); //筛选下来框
                var suggest = filter.find('.suggest');
                var body = $('body');
                //获取焦点
                ipt.focus(function() {
                    $(this).val('');
                    body.append('<div class="overbg" style="z-index:1;"></div>');
                });

                //失去焦点如果为空则显示原始值
                ipt.blur(function() {
                    var _val = $(this).val();
                    if (_val == '') {
                        $(this).val(iptVal);
                    }
                    $('.overbg').remove();
                });

                //2.点击筛选
                filterAll.on('click', function() {
                    var _this = $(this);
                    var _index = _this.index();
                    filterItem.eq(_index)
                            .css('left', parseInt(_this.position().left) + 'px')
                            .show()
                            .siblings('.item')
                            .hide();
                });

                filterItem.each(function() {
                    var _this = $(this);
                    var level = $(this).find('.level');
                    var level2 = $(this).find('.level2');
                    var _li = level.find('li'); //分类

                    _li.hover(function() {
                        $(this).addClass('active').siblings('li').removeClass('active');

                        $("#catecontent").empty();
                        $("#citycontent").empty();

                        if (typeof(category_all[$(this).index()].children) != "undefined") {
                            $.each(category_all[$(this).index()].children, function(index, content) {
                                $("#catecontent").append("<li data-id=\"" + content.gbCategory + "\" >" + content.name + "</li>");
                            });

                            //点击二级分类
                            $("#catecontent").find('li').on('click', function() {
                                filterAll.eq(0).text($(this).html());
                                _this.hide();
                               $("#gb_category_code").val($(this).attr("data-id"));
                                //searchData_();
                            });
                        }

                        if (typeof(dic_arr) != "undefined") {
                            $.each(dic_arr, function(index, content) {
                                $("#citycontent").append("<li data-id=\"" + content.code + "\"  >" + content.name + "</li>");
                            });

                            //国家级
                            $("#country").find('li').on('click', function() {
                                filterAll.eq(1).text($(this).html());
                                _this.hide();
                                $("#area_code").val("");
                                // searchData_();
                            });

                            //一级城市
                            $("#citycontent").find('li').on('click', function() {
                                filterAll.eq(1).text($(this).html());
                                _this.hide();
                                $("#area_code").val($(this).attr("data-id"));
                                //searchData_();
                            });
                        }
                        level2.show();
                    });
                });


                //点击一级类别

                //3.阻止点击自身关闭
                filter.on('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                });

                //4.点击自身之外的地方关闭下拉框
                $(document).on("click", function() {
                    filterItem.hide();
                    filterFixed.slideUp('fast');
                });
                //自动提示
                body.find('.overbg').on('click', function() {
                    filterItem.hide();
                    filterFixed.slideUp('fast');
                    suggest.hide();
                    $(this).remove();
                    body.css('overflow', '');
                });

            },
        };
        searchPage.init();
    });
    function submit(){

        $(".form").ajaxSubmit();
    }
</script>

</html>