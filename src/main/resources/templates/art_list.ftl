<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>文章详情页</title>
    <link rel="stylesheet" href="../../css/reset.css"/>
    <style>
        .main{
            width: 800px;
            min-height: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #ffffff;
            border: 1px solid #eee
        }
        .row{
            padding:5px 0;
        }
        .row span{
            width: 100px;
        }
        .row input{
            width: 500px;
            border: 1px solid #00496b;
            padding: 5px;
        }
        button{
            border: 1px solid #00496b;
            padding: 5px;
            width: 100px;
        }
        .new{
            display: inline-block;
            background: buttonface;
            border: 1px solid #00496b;
            padding: 2px 5px;
            width: 90px;
            text-align: center;
            vertical-align: middle;
        }
        .art-list{
            border: 1px solid #00496b;
            padding: 10px;
        }
        .art-list li{
            padding: 5px 0;
        }
        .art-list li .title{
             width: 60%;
             overflow: hidden;
             text-overflow: ellipsis;
             white-space: nowrap;
             display: inline-block;
             line-height: 21px;
             vertical-align: middle;
         }
        .art-list li:hover{
            color: blue;
        }
        .art-list li a,.art-list li i{
            float: right;
            border: 1px solid #00496b;
            width: 60px;
            text-align: center;
            margin: -1px 5px;
            color: #000;
        }
        .tag-list{
            margin: 20px 0;
        }
        .tag-list li{
            border: 1px solid #00496b;
            min-width: 80px;
            text-align: center;
            display: inline-block;
            cursor: pointer;
        }
        .tag-list li.active{
            color: #ffffff;
            background: #00496b;
        }
    </style>
</head>
<body style="background: #fafafa">
<div class="main">
    <div class="row clearfix">
        <span>关键字：</span>
        <input class="key" type="text" >
        <button class="search" type="button">搜索</button>
        <a class="new" href="/distinctclinic/article/">新增</a>
    </div>
    <div class="art-panel">
        文章列表：
        <ul class="art-list">
            <li>
                <span class="title">我是标题我是标题我是标题我是标题我是标题我是标题我是标题我是标题我是标题我是标题</span>
                <span class="datetime">2015-12-12</span>
                <a href="/">编辑</a>
                <i class="edit">删除</i>
            </li>
            <li>
                <span class="title">我是标题我是标题</span>
                <span class="datetime">2015-12-12</span>
                <a href="/">编辑</a>
                <i class="edit">删除</i>
            </li>
            <li>
                <span class="title">我是标题我是标题</span>
                <span class="datetime">2015-12-12</span>
                <a href="/">编辑</a>
                <i class="edit">删除</i>
            </li>
            <li>
                <span class="title">我是标题我是标题</span>
                <span class="datetime">2015-12-12</span>
                <a href="/">编辑</a>
                <i class="edit">删除</i>
            </li>
            <li>
                <span class="title">我是标题我是标题</span>
                <span class="datetime">2015-12-12</span>
                <a href="/">编辑</a>
                <i class="edit">删除</i>
            </li>
            <li>
                <span class="title">我是标题我是标题</span>
                <span class="datetime">2015-12-12</span>
                <a href="/">编辑</a>
                <i class="edit">删除</i>
            </li>
        </ul>
    </div>
    <div>
        <ul class="tag-list">
            标签列表：
            <li class="active">tag1</li>
            <li>tag2</li>
            <li>tag3</li>
            <li>tag4</li>
            <li>tag5</li>
            <li>tag6</li>
        </ul>
        <button id="submit" type="button" class="">提交</button>
    </div>
</div>
<script type="application/javascript" src="../../js/jquery.js"></script>
<script type="application/javascript">
    $(function(){
        var $tags = $('.tag-list').find('li');
        //选择标签
        $tags.on('click', function(){
            $(this).toggleClass('active');
        });
    })
</script>
</body>
</html>