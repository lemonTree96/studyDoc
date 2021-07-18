 &emsp; &emsp;Git 是一个开源的**分布式版本控制系统**，用于敏捷高效地处理任何或小或大的项目。分布式版本控制系统没有“中央服务器”，每个人的电脑都是一个完整的版本库，其安全性要高于集中式版本控制系统。GIt的特点为：安全性高，不依赖网络(没有网络时也可以Commit)

&emsp;&emsp; <font color=SlateBlue><u>**Q1.Git与SVN的区别 ？**</u></font>
&emsp;&emsp;  ① SVN是集中式的。SVN必须有一个服务器版本库放在中央服务器中，所有的开发人员都是和中央服务器进行交互。在SVN中,当提交代码时候，它将直接记录到（同步）中心版本库，在没有网络时无法提交代码。具体的开发流程就是：首先从服务器中获取最新的版本，开发完后，再将自己所做的工作推送到中央服务器中，
 &emsp;&emsp;  ② Git是分布式的。Git没有中央服务器，每个人的电脑都有一个完整的版本库，因此采用Git在两者之间开发时是不需要联网的，只需要将修改内容相互交换即可。在大型的开发中，Git是有一个”中心服务器“，仅仅是为了方便修改内容的交换，该”中心服务器“的地位与每个人所用的电脑是同等级别的，在没有网络的情况下同样可以Commit，查看历史记录，创建分支，合并等操作，等到网络再次连接上Push到Server端即可。

 &emsp;&emsp; <font color=SlateBlue><u>**Q2. Git怎么保证历史记录不可篡改？**</u></font>
&emsp;&emsp; 通过`SHA1`哈希算法和哈系树来保证。当修改了历史变更记录上一个文件的内容，那么这个问卷的`Blob`对象的`SHA1`哈希值就变了，与之相关的`Tree`对象的`SHA1`也需要改变，`Commit`对象的`SHA1`也要变，这个commit操作之后的所有`Commit SHA1`值也要跟着改变。

###  一.  Git的工作流程及结构
#### 1.1 Git的工作流程
&emsp; &emsp;Git 作为一个源码管理系统，需要多人协作。那么协作必须有一个规范的工作流程，对于Git，其工作流程如下图所示：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210207224730261.png#pic_center)
&emsp; &emsp; 项目的主分支master用于存放对外开放的版本，任何时候在这个分支拿到的，都是稳定的分布版。其他的分支则是用于开发的分支。
&emsp; &emsp; git的工作流程如下：
&emsp;&emsp;&emsp;  1. 在工作目录中添加、修改文件；对应的文件状态为 -- 已修改(modified)
&emsp;&emsp;&emsp; 2. 将需要进行版本管理的文件放入暂存区域；对应的文件状态为 -- 已暂存(staged)
&emsp;&emsp;&emsp;  3. 将暂存区域的文件提交到git仓库；对应的文件状态为 -- 已提交(commited)
#### 1.2 Git结构与命令
▍ **1.2.1 Git结构** 
&emsp; &emsp; Git在结构上主要分为四个部分：远程主机，本地仓库(版本库)，暂存区，工作区。
&emsp;&emsp;&emsp; ● **远程主机**：托管代码的服务器。
&emsp;&emsp;&emsp; ● **本地仓库(版本库)**：安全存放数据的位置，这里面包含了提交的所有版本的数据。其中HEAD指向最新放入仓库的版本。
&emsp;&emsp;&emsp; ● **暂存区**：用于临时存放你的改动，事实上它只是一个文件，保存即将提交到文件列表信息。
&emsp;&emsp;&emsp; ● **工作区**：就是平时存放项目代码的地方。![在这里插入图片描述](https://img-blog.csdnimg.cn/20210207230649112.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
▍ **1.2.2 Git命令** 
&emsp; &emsp; Git有主要以下几个操作：`git clone`，`git remote`，`git fetch`，`git branch`，`git pull`，`git push`，`git checkout`，`git add`，`git commit`，`git stash`。
&emsp;&emsp;●  git  clone: 从远程主机中克隆一个版本库到本地仓库
&emsp;&emsp;&emsp; [`git clone <版本库的网址> <本地目录名>`] 
&emsp;&emsp;●  git remote:用于管理主机名
&emsp;&emsp;&emsp; [`git remote -v`] 列出所有的主机名及网址
&emsp;&emsp;&emsp; [`git remote show <主机名>`] 查看主机的详细信息
&emsp;&emsp;&emsp; [`git remote add <主机名> <网址>`]  添加远程主机
&emsp;&emsp;&emsp; [`git remote rm <主机名>`] 删除远程主机
&emsp;&emsp;&emsp; [`git remote rename <原主机名> <新主机名>`] 远程主机重命名
&emsp;&emsp;● git fetch: 将远程主机的更新取回本地仓库
&emsp;&emsp;&emsp; [`git fetch <远程主机名> <分支名>`]  拉取特定分支的更新到本地
&emsp;&emsp;● git branch: 查看分支
&emsp;&emsp;&emsp; [`git branch -r`] 查看远程分支
&emsp;&emsp;&emsp; [`git branch -a`] 查看所有分支
&emsp;&emsp;● git checkout：创建新的分支
&emsp;&emsp;&emsp; [`git checkout -b <分支名> <分支1>`] 在分支1的基础创建新分支
&emsp;&emsp;●  git pull：取回远程主机某个分支的更新，再与本地的指定分支合并
&emsp;&emsp;&emsp; [`git pull <远程主机名> <远程分支名>:<本地分支名>`] 取远程主机的分支的更新，与本地的指定分支合并
&emsp;&emsp;● git stash：可用于将目前还不想提交的但是已经修改内容的工作区临时保存至堆栈区，后续可以在某个分支(可以跨分支)上恢复出堆栈中的内容。
&emsp;&emsp;&emsp;[`git stash <save message>`] 将未提交的修改(工作区和暂存区)保存至堆栈中，用于后续恢复当前工作目录
&emsp;&emsp;&emsp;[`git stash list`] 查看当前stash中的内容
&emsp;&emsp;&emsp;[`git stash pop`] 将当前stash中的内容弹出，并应用到当前分支对应的工作目录上。该命令将堆栈中最近保存的内容删除(栈顶内容被删除)。
&emsp;&emsp;&emsp;[`git stash apply`] 将堆栈中的内容应用到当前目录，该命令不会将内容从堆栈中删除。
&emsp;&emsp;&emsp;[`git stash drop + 名称`] 从堆栈中移除某个指定的stash
###  二.  Git的开发流程
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210214162431733.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center)
#### 2.1 Git开发的前置准备
&emsp; &emsp;  ① 初始化本地仓库。如`git init`
&emsp; &emsp;  ② 创建本地(新)分支。 如 ` git checkout -b <本地(新)分支名称>`
&emsp; &emsp;  ③ 设置远程主机名称，并绑定远程主机。如` git remote add origin <远程主机Git地址>`。若主机名称被占用，可以删除被占用的主机名称，`git remote remove origin`
&emsp; &emsp; ④ 设置本地分支的远程跟踪 `git branch --set-upstream-to=origin/<branch> <本地分支名称>`
#### 2.2 Git开发的具体过程
&emsp; &emsp;  ① 将master分支(或其他待开发分支)clone到本地分支。如:`git clone -b <分支名称A> <git地址>`
&emsp; &emsp; ② clone到本地的分支不能直接在上面开发，而是需要另外创建分支。如:`git checkout -b <分支名称B>`
&emsp; &emsp; ③ 在创建的分支B上开发完后， 将代码提交到本地仓库。
&emsp;&emsp;&emsp;  ● 当对分支的内容进行修改，增加文件，删除文件时，通过`git status`可以显示出当前的状态。此时文件存储在工作区中。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210321215027250.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center  =460x160)
&emsp;&emsp;&emsp;  ●  `git add .`	： ".“的意思就是保存添加所有修改到暂存区
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210321220318964.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center  =320x120)
&emsp;&emsp;&emsp;  ● `git commit -m “注释” `:	将暂存区中的修改提交到本地仓库
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210321220522397.png#pic_center  =260x60)

&emsp; &emsp; ④ 将B分支合并到A分支中。
&emsp;&emsp;&emsp;  ● `git checkout <分支名称A>` : 先切换到分支B。
&emsp;&emsp;&emsp;  ● `git pull origin <分支名称A>` :从远端拉取最新的代码，因为当你合并分支的时候，可能其他人提交了新的内容，所以要先拉取一次最新的代码
&emsp;&emsp;&emsp;  ● `git merge <分支名称B>` ：合并分支B，并处理合并冲突
&emsp;&emsp;  ⑤ 将本地分支push到远程分支
&emsp;&emsp;&emsp;  ● `git push origin <本地分支A>:<远程分支>`：将本地分支推送到远。在第一次`push`分支时，可能会当前分支没有指定上游分支的情况，这时通过`git push --set-upstream origin <本地分支>`

#### 2.3 Git撤销过程
&emsp; &emsp; 当通过`commit` 提交代码后，发现这一次commit的内容是有错误的， 通常有两种处理方法：
&emsp;&emsp;&emsp; ① 修改错误内容，再次commit一次，但是每提交一次，都会多一次`commit`记录；
&emsp;&emsp;&emsp; ② 使用`git reset` 撤销上一次的`commit`。
▍ **2.3.1 Git Reset 撤销** 
&emsp; &emsp; `git reset` 命令用于回退版本，可以指定退回某一次提交的版本。命令语法为 `git reset [--soft | --mixed | --hard] [HEAD]`
&emsp; &emsp;`git reset`分为三个模式：`soft`,`mixed`,`hard`
&emsp; &emsp; ●  `git reset --hard`：用于重置暂存区和工作目录(区)，即没有commit的修改会被全部擦掉。
&emsp; &emsp; ● `git reset --sort`：保留工作目录(区)，并把重置 HEAD 所带来的新的差异放进暂存区。此模式会保留工作目录(区)的內容，也会保留暂存区的內容，让 index 暂存区与 workingspace 工作目录的內容是一致的。而原始节点与reset节点之间的差异变更集合会存在与index暂存区中，可以直接通过`git commit`将暂存区中的内容提交到 repository 本地仓库中。
&emsp; &emsp; ● ` git reset --mixed`:--mixed是reset的默认参数，其保留工作目录，并清空暂存区。工作目录的修改、暂存区的内容以及由 reset 所导致的新的文件差异，都会被放进工作目录。
***
###  三.  Git的底层实现原理
&emsp; &emsp;  在学习Git的底层原理时，我们按照Git的操作流程来一步步展示其原理。
#### 3.1 Git 对象类型
&emsp; &emsp; Git 包含4种对象类型：`Blob`，`Tree`，`Commit`，`Tag`。
&emsp; &emsp;  ● `Blob`对象：只用于存储单个文件内容，一般都是二进制的数据文件，不包含任何其他文件信息，如文件名和其他元数据是存放在`Tree`对象中的。
&emsp; &emsp;  ● `Tree`对象：对应文件系统的目录结构，里面主要有：子目录 (tree)，文件列表 (blob)，文件类型以及一些数据文件权限模型等。
&emsp; &emsp;  ●`Commit`对象：是一次修改的集合，当前所有修改的文件的一个集合。`Commit`对象是修改过的文件集的一个快照，每进行一次commit操作，修改过的文件将会被提交到 local repository 中。通过`Commit`对象，在版本化中可以检索出每次修改内容，是版本化的基石。
&emsp; &emsp;  ●`Tag`对象：Tag是一个"固化的分支"，一旦打上tag之后，这个tag代表的内容将永远不可变，因为tag只会关联当时版本库中最后一个commit对象。对于分支来说，随着不断的提交，内容会不断的改变，分支指向的最后一个commit 不断改变。所以一般应用或者软件版本的发布一般用tag。

#### 3.2 Git 底层原理
&emsp; &emsp; Git的存储原理其本质上是**一个`key-value`的数据库加上默克尔树形成的有向无环图(DAG)**。

▍ **3.2.1 Git 是如何暂存信息的(Git add)** 
&emsp; &emsp; `git add`会对先每一个文件计算checksum(校验和)，然后把当前版本的文件快照保存到本地Git仓库中(Git 使用`blob`类型对象，存储文件内容快照)，并将校验和加入暂存区域。Git 使用`SHA-1`算法计算数据的校验和，通过对文件的内容或目录的结构计算出一个SHA-1哈希值，作为指纹字符串。该字串由40个十六进制字符,并将此结果作为数据文件的唯一标识和索引。<font color=red>所有保存在Git 数据库中的东西都是用此哈希值来作索引的，不是靠文件名。</font>
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210221153900193.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)
▍ **3.2.2 Git 是如何提交信息的(Git commit)** 
&emsp; &emsp; 在使用`git commit`新建一个提交对象前，<font color=green>Git会先计算每一个子目录(项目根目录)的校验和，然后在Git 仓库中将这些目录保存为**树(tree)对象**</font>。之后<font color=green>Git创建的 **`Commit`对象**，除了包含相关提交信息以外，还包含着指向这个树对象(项目根目录)的指针</font>，如此它就可以在将来需要的时候，重现此次快照的内容了。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210221153818198.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70)
 &emsp;&emsp; <font color=SlateBlue><u>**Q1.每次commit，Git储存的是全新的文件快照还是储存文件的变更部分？**</u></font>
&emsp; &emsp;  Git储存的是全新的文件快照，而不是文件的变更记录。如果Git储存的是问卷的变更部分，那么为了拿到一个commit的内容，Git只能从第一个commit开始，然后一直计算变更，直到目标commit，这会花费很长时间。而相反，Git采用的储存全新文件快照的方法能使这个操作变得很快，直接从快照里面拿取内容即可。


▍ **3.2.3 Git 本地仓库如何存储信息** 
&emsp; &emsp;  通过`git add`，`git commit`，将修改的文件添加到暂存区(index)和本地仓库中。此时，本地仓库，暂存区和工作空间的状态如下：![在这里插入图片描述](https://img-blog.csdnimg.cn/20210221153327516.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_6,color_FFFFFF,t_70#pic_center =450x300 )
▍ **3.2.4 Git 如何更新文件信息** 
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210221153241352.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70 =1250x300)
▍ **3.2.5 Git 分支** 
&emsp; &emsp; 在Git中存在一个特殊的指针`HEAD`，该指针是一个可以在任何分支和版本移动的指针，通过移动指针我们可以将数据还原至任何版本。每做一次`Commit`操作都会导致git更新一个版本，`HEAD`指针也跟着自动移动，因此`HEAD`指针默认指向当前分支的最后一次提交。
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210221162904429.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80Mjk2Mzk2OQ==,size_16,color_FFFFFF,t_70#pic_center =560x180)





