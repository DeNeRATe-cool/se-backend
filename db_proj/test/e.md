# 数据库设计

> `identity` 规范
>
> | 教师 | 学生 | 助教 |
> | ---- | ---- | ---- |
> | 0    | 1    | 2    |

### `t_user`：用户

包含教师/助教/学生

`username`：账号（学工号）

`name`：姓名

`identity`：教师/学生

| user_id | username     | password     | name         | mail         | birthday | identity |
| ------- | ------------ | ------------ | ------------ | ------------ | -------- | -------- |
| INT     | VARCHAR(255) | VARCHAR(255) | VARCHAR(255) | VARCHAR(255) | DATE     | INT      |

### `t_course`：课程

`syllabus`：课程大纲

`assMethod`：考核方式

`score`：学分

`time`：学时

| course_id | name         | creator_id | syllabus | assMethod    | score | time |
| --------- | ------------ | ---------- | -------- | ------------ | ----- | ---- |
| INT       | VARCHAR(255) | INT        | TEXT     | VARCHAR(255) | FLOAT | INT  |

### `t_class`：班级

`class_code` 为创建之后生成的班级统一格式的 id, 用于学生搜索加入

| class_id | name         | course_id | class_code   |
| -------- | ------------ | --------- | ------------ |
| INT      | VARCHAR(255) | INT       | VARCHAR(255) |

### `t_process`：进度

| process_id | name         | course_id | class_id | time |
| ---------- | ------------ | --------- |-------- | ---- |
| INT        | VARCHAR(255) | INT       | INT | DATE |

### `t_resource`：资源

【课程共有资源表】`process_id`：为 `-1` 时，为公共资源, 对于上传时为公共资源的资源, 存储两份, 一份在进度, 一份在公共区

`type`：取文件的后缀，若没有后缀，默认为 `txt`

`res_id`：主键，唯一

`res_code`：同一项资源，支持历史更新，更新前后该值不变，通过 `date` 查看最新的

|res_id| res_code | name | process_id| course_id | type | data | date | tag |
|---| ------ | --- |---------- |---| ---- | ---- |---|---|
|INT| VARCHAR(255) | VARCHAR(255) | INT |INT| VARCHAR(255) | BLOB | DATETIME | VARCHAR(255) |

### `t_exer`：任务（练习）

【新建任务后存在于题库中的任务模板】`class_id & course_id`：为 `-1` 时，表示该项存在题库中，作为模板

`problems`：`JSON` 存取 `prob_id` 列表

`is_public`：是否在题库中公开，可被克隆

`is_multi`：是否允许多次提交

| exer_id | class_id | course_id | creator_id | begin_time | end_time | is_public | name         | is_multi | score|
| ------- | -------- | --------- | ---------- | ---------- | -------- | --------- | ------------ | -------- |---|
| INT     | INT      | INT       | INT        | DATETIME   | DATETIME | BOOLEAN   | VARCHAR(255) | BOOLEAN  |INT|

### `t_prob`：题目

`type` 为 题目类型：选择题；判断题；解答题；编程题

`description`：题目

`content`：选项

`answer`：答案（如 `A`）

`analysis`：详细解析

| prob_id | is_public | type | creator_id | create_time | description | content | answer | analysis |
| ------- | --------- | ---- | ---------- | ----------- | ----------- | ------- | ------ | -------- |
| INT     | BOOLEAN   | INT  | INT | DATETIME | VARCHAR(255) | VARCHAR(255) | VARCHAR(255) | VARCHAR(255) |

### `t_user_course_class`：用户-课程-班级表

【课程班级表】`user_id`：用户 `id` 为 `-1` 时代表课程-班级关系，可查询课程对应的所有班级

【未分配班级时老师/助教 - 课程表】`class_id`：班级 `id` 为 `-1` 时代表老师和助教进入课程之后还没有分配班级

注: 对于助教, 被老师通过课程邀请进入后直接赋值

| user_id | course_id | class_id | identity |
| ------- | --------- | -------- | -------- |
| INT     | INT       | INT      | INT      |

### `t_stu_prob_exer`：学生-题目-任务表

【任务与题目的标准分表】`stu_id`：学生 `id` 为 `-1` 时代表标准得分分配

【学生对应任务的总分表】`prob_id`：题目 `id` 为 `-1` 时学生任务总分

`submit`：学生作答

`is_finish`：学生是否完成该任务

`is_check`：学生的该任务是否已被批改

`index`：题目在任务中的题号

| stu_id | prob_id | exer_id | score | comment      | submit       | is_finish | is_check | index |
| ------ | ------- | ------- | ----- | ------------ | ------------ | --------- | -------- | ----- |
| INT    | INT     | INT     | INT   | VARCHAR(255) | VARCHAR(255) | BOOLEAN   | BOOLEAN  | INT   |

### `t_wrong`：错题表

| stu_id | prob_id |exer_id|
| ------ | ------- |---|
| INT    | INT     |INT|

### `t_collect`：收藏表

| stu_id | prob_id |
| ------ | ------- |
| INT    | INT     |

### `t_tag`：标签表

| tag_id | name         |
| ------ | ------------ |
| INT    | VARCHAR(255) |

