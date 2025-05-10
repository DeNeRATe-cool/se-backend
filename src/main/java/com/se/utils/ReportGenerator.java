package com.se.utils;

import com.se.entity.Exercise;
import com.se.entity.User;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGenerator {

    /**
     * 生成学生练习报告
     *
     * @param user 学生信息
     * @param exercises 练习列表
     * @param rankings 排名列表
     * @param scores 成绩列表
     * @return 生成的HTML文件路径
     */
    public static String generateExerciseReport(User user, List<Exercise> exercises,
                                                List<Integer> rankings, List<Integer> scores) throws IOException {

        if (exercises.size() != rankings.size() || exercises.size() != scores.size()) {
            throw new IllegalArgumentException("练习、排名和成绩列表长度必须一致");
        }

        // 计算统计数据
        int highestRank = Collections.min(rankings);
        int lowestRank = Collections.max(rankings);
        double averageScore = scores.stream().mapToInt(Integer::intValue).average().orElse(0);
        int totalExercises = exercises.size();

        int highestScore = Collections.max(scores);
        int lowestScore = Collections.min(scores);
        double averageRank = rankings.stream().mapToInt(Integer::intValue).average().orElse(0);

        // 计算趋势 - 分数是否在提高
        boolean isScoreImproving = false;
        if (scores.size() >= 2) {
            int firstHalfAvg = scores.subList(0, scores.size() / 2).stream().mapToInt(Integer::intValue).sum() / (scores.size() / 2);
            int secondHalfAvg = scores.subList(scores.size() / 2, scores.size()).stream().mapToInt(Integer::intValue).sum() / (scores.size() - scores.size() / 2);
            isScoreImproving = secondHalfAvg > firstHalfAvg;
        }

        // 准备图表数据
        String exerciseNames = exercises.stream()
                .map(e -> "'" + e.getName() + "'")
                .collect(Collectors.joining(", "));

        String scoreData = scores.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        String rankingData = rankings.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        // 生成HTML内容
        StringBuilder exerciseTable = new StringBuilder();
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            exerciseTable.append("<tr>")
                    .append("<td>").append(exercise.getName()).append("</td>")
                    .append("<td>").append(scores.get(i)).append("</td>")
                    .append("<td>").append(rankings.get(i)).append("</td>")
                    .append("<td>").append(new SimpleDateFormat("yyyy-MM-dd").format(exercise.getBegin_time()))
                    .append(" ~ ").append(new SimpleDateFormat("yyyy-MM-dd").format(exercise.getEnd_time())).append("</td>")
                    .append("</tr>");
        }

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"zh-CN\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>学生练习报告 - " + user.getName() + "</title>\n" +
                "    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n" +
                "    <style>\n" +
                "        @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');\n" +
                "        :root {\n" +
                "            --primary: #6a5acd;\n" +
                "            --primary-light: #8677d9;\n" +
                "            --secondary: #5d4aaa;\n" +
                "            --accent: #9c27b0;\n" +
                "            --accent-light: #ba68c8;\n" +
                "            --success: #2e7d32;\n" +
                "            --warning: #e65100;\n" +
                "            --background: #f9f9f9;\n" +
                "            --card-bg: #ffffff;\n" +
                "            --text: #2c2c2c;\n" +
                "            --text-light: #6c6c6c;\n" +
                "            --border: #e0e0e0;\n" +
                "            --shadow: 0 8px 30px rgba(0, 0, 0, 0.05);\n" +
                "            --shadow-hover: 0 10px 40px rgba(0, 0, 0, 0.08);\n" +
                "        }\n" +
                "        * {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            box-sizing: border-box;\n" +
                "            font-family: 'Poppins', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
                "        }\n" +
                "        body {\n" +
                "            background-color: var(--background);\n" +
                "            color: var(--text);\n" +
                "            line-height: 1.6;\n" +
                "            font-weight: 400;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 1200px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 2.5rem;\n" +
                "        }\n" +
                "        header {\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 3rem;\n" +
                "            padding: 3rem 2rem;\n" +
                "            background: linear-gradient(135deg, var(--primary), var(--accent));\n" +
                "            color: white;\n" +
                "            border-radius: 16px;\n" +
                "            box-shadow: var(--shadow);\n" +
                "            position: relative;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        header::before {\n" +
                "            content: '';\n" +
                "            position: absolute;\n" +
                "            top: 0;\n" +
                "            left: 0;\n" +
                "            width: 100%;\n" +
                "            height: 100%;\n" +
                "            background: url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSI1NiIgaGVpZ2h0PSIxMDAiPgo8cmVjdCB3aWR0aD0iNTYiIGhlaWdodD0iMTAwIiBmaWxsPSIjZmZmZmZmMDUiPjwvcmVjdD4KPHBhdGggZD0iTTI4IDY2TDAgNTBMMCAxNkwyOCAwTDU2IDE2TDU2IDUwTDI4IDY2TDI4IDEwMCIgZmlsbD0ibm9uZSIgc3Ryb2tlPSIjZmZmZmZmMTAiIHN0cm9rZS13aWR0aD0iMiI+PC9wYXRoPgo8cGF0aCBkPSJNMjggMEwyOCAzNEw1NiA1MEw1NiAxNkwyOCAwWiIgZmlsbD0iI2ZmZmZmZjA1IiBzdHJva2U9IiNmZmZmZmYxMCIgc3Ryb2tlLXdpZHRoPSIyIj48L3BhdGg+Cjwvc3ZnPg==');\n" +
                "            opacity: 0.1;\n" +
                "            z-index: 0;\n" +
                "        }\n" +
                "        header > * {\n" +
                "            position: relative;\n" +
                "            z-index: 1;\n" +
                "        }\n" +
                "        h1 {\n" +
                "            font-size: 2.8rem;\n" +
                "            font-weight: 600;\n" +
                "            margin-bottom: 0.8rem;\n" +
                "            letter-spacing: -0.5px;\n" +
                "        }\n" +
                "        header p {\n" +
                "            font-size: 1.2rem;\n" +
                "            opacity: 0.9;\n" +
                "            margin-bottom: 1.5rem;\n" +
                "        }\n" +
                "        .student-info {\n" +
                "            display: flex;\n" +
                "            justify-content: space-around;\n" +
                "            margin-top: 2rem;\n" +
                "            flex-wrap: wrap;\n" +
                "            gap: 1.5rem;\n" +
                "        }\n" +
                "        .student-info div {\n" +
                "            text-align: center;\n" +
                "            background-color: rgba(255, 255, 255, 0.15);\n" +
                "            backdrop-filter: blur(5px);\n" +
                "            padding: 1rem 1.5rem;\n" +
                "            border-radius: 12px;\n" +
                "            min-width: 150px;\n" +
                "        }\n" +
                "        .student-info h3 {\n" +
                "            font-size: 0.9rem;\n" +
                "            font-weight: 500;\n" +
                "            margin-bottom: 0.5rem;\n" +
                "            opacity: 0.8;\n" +
                "            text-transform: uppercase;\n" +
                "            letter-spacing: 1px;\n" +
                "        }\n" +
                "        .student-info p {\n" +
                "            font-size: 1.3rem;\n" +
                "            font-weight: 600;\n" +
                "            margin: 0;\n" +
                "        }\n" +
                "        .card {\n" +
                "            background: var(--card-bg);\n" +
                "            border-radius: 16px;\n" +
                "            padding: 2rem;\n" +
                "            margin-bottom: 2.5rem;\n" +
                "            box-shadow: var(--shadow);\n" +
                "            transition: transform 0.3s ease, box-shadow 0.3s ease;\n" +
                "        }\n" +
                "        .card:hover {\n" +
                "            transform: translateY(-5px);\n" +
                "            box-shadow: var(--shadow-hover);\n" +
                "        }\n" +
                "        .card h2 {\n" +
                "            color: var(--primary);\n" +
                "            margin-bottom: 1.5rem;\n" +
                "            padding-bottom: 0.8rem;\n" +
                "            border-bottom: 2px solid var(--border);\n" +
                "            font-weight: 600;\n" +
                "            font-size: 1.5rem;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        .card h2::before {\n" +
                "            content: '';\n" +
                "            display: inline-block;\n" +
                "            width: 8px;\n" +
                "            height: 24px;\n" +
                "            background: linear-gradient(to bottom, var(--primary), var(--accent));\n" +
                "            margin-right: 12px;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .stats-container {\n" +
                "            display: grid;\n" +
                "            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));\n" +
                "            gap: 1.8rem;\n" +
                "            margin-bottom: 2.5rem;\n" +
                "        }\n" +
                "        .stat-card {\n" +
                "            background: var(--card-bg);\n" +
                "            border-radius: 16px;\n" +
                "            padding: 1.8rem;\n" +
                "            text-align: center;\n" +
                "            box-shadow: var(--shadow);\n" +
                "            transition: transform 0.3s ease, box-shadow 0.3s ease;\n" +
                "            position: relative;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        .stat-card::after {\n" +
                "            content: '';\n" +
                "            position: absolute;\n" +
                "            bottom: 0;\n" +
                "            left: 0;\n" +
                "            width: 100%;\n" +
                "            height: 4px;\n" +
                "            background: linear-gradient(to right, var(--primary), var(--accent));\n" +
                "            transform: scaleX(0);\n" +
                "            transform-origin: left;\n" +
                "            transition: transform 0.3s ease;\n" +
                "        }\n" +
                "        .stat-card:hover {\n" +
                "            transform: translateY(-8px);\n" +
                "            box-shadow: var(--shadow-hover);\n" +
                "        }\n" +
                "        .stat-card:hover::after {\n" +
                "            transform: scaleX(1);\n" +
                "        }\n" +
                "        .stat-card h3 {\n" +
                "            color: var(--text-light);\n" +
                "            font-size: 1rem;\n" +
                "            font-weight: 500;\n" +
                "            margin-bottom: 0.8rem;\n" +
                "        }\n" +
                "        .stat-card p {\n" +
                "            font-size: 2.2rem;\n" +
                "            font-weight: 700;\n" +
                "            color: var(--primary);\n" +
                "            margin-bottom: 0.5rem;\n" +
                "        }\n" +
                "        .stat-card .trend {\n" +
                "            font-size: 0.9rem;\n" +
                "            color: var(--text-light);\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            margin-top: 0.5rem;\n" +
                "        }\n" +
                "        .trend.up {\n" +
                "            color: var(--success);\n" +
                "        }\n" +
                "        .trend.down {\n" +
                "            color: var(--warning);\n" +
                "        }\n" +
                "        .trend::before {\n" +
                "            content: '';\n" +
                "            display: inline-block;\n" +
                "            width: 0;\n" +
                "            height: 0;\n" +
                "            margin-right: 5px;\n" +
                "        }\n" +
                "        .trend.up::before {\n" +
                "            border-left: 5px solid transparent;\n" +
                "            border-right: 5px solid transparent;\n" +
                "            border-bottom: 8px solid var(--success);\n" +
                "        }\n" +
                "        .trend.down::before {\n" +
                "            border-left: 5px solid transparent;\n" +
                "            border-right: 5px solid transparent;\n" +
                "            border-top: 8px solid var(--warning);\n" +
                "        }\n" +
                "        .chart-container {\n" +
                "            display: grid;\n" +
                "            grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));\n" +
                "            gap: 2.5rem;\n" +
                "            margin-bottom: 2.5rem;\n" +
                "        }\n" +
                "        .chart-card {\n" +
                "            background: var(--card-bg);\n" +
                "            border-radius: 16px;\n" +
                "            padding: 2rem;\n" +
                "            box-shadow: var(--shadow);\n" +
                "            transition: transform 0.3s ease, box-shadow 0.3s ease;\n" +
                "        }\n" +
                "        .chart-card:hover {\n" +
                "            transform: translateY(-5px);\n" +
                "            box-shadow: var(--shadow-hover);\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin-top: 1.5rem;\n" +
                "            border-radius: 8px;\n" +
                "            overflow: hidden;\n" +
                "        }\n" +
                "        th, td {\n" +
                "            padding: 1rem 1.5rem;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        th {\n" +
                "            background-color: var(--primary);\n" +
                "            color: white;\n" +
                "            font-weight: 500;\n" +
                "            text-transform: uppercase;\n" +
                "            font-size: 0.85rem;\n" +
                "            letter-spacing: 1px;\n" +
                "        }\n" +
                "        tr:nth-child(even) {\n" +
                "            background-color: rgba(106, 90, 205, 0.05);\n" +
                "        }\n" +
                "        tr {\n" +
                "            border-bottom: 1px solid var(--border);\n" +
                "            transition: background-color 0.2s ease;\n" +
                "        }\n" +
                "        tr:hover {\n" +
                "            background-color: rgba(106, 90, 205, 0.1);\n" +
                "        }\n" +
                "        td {\n" +
                "            color: var(--text);\n" +
                "        }\n" +
                "        .summary-section {\n" +
                "            margin-bottom: 2.5rem;\n" +
                "            padding: 2rem;\n" +
                "            background-color: var(--card-bg);\n" +
                "            border-radius: 16px;\n" +
                "            box-shadow: var(--shadow);\n" +
                "        }\n" +
                "        .summary-section h2 {\n" +
                "            color: var(--primary);\n" +
                "            margin-bottom: 1.5rem;\n" +
                "            font-weight: 600;\n" +
                "            font-size: 1.5rem;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "        .summary-section h2::before {\n" +
                "            content: '';\n" +
                "            display: inline-block;\n" +
                "            width: 8px;\n" +
                "            height: 24px;\n" +
                "            background: linear-gradient(to bottom, var(--primary), var(--accent));\n" +
                "            margin-right: 12px;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .summary-content {\n" +
                "            display: flex;\n" +
                "            flex-wrap: wrap;\n" +
                "            gap: 2rem;\n" +
                "            justify-content: space-between;\n" +
                "        }\n" +
                "        .summary-item {\n" +
                "            flex: 1;\n" +
                "            min-width: 250px;\n" +
                "        }\n" +
                "        .summary-item h3 {\n" +
                "            font-size: 1.1rem;\n" +
                "            color: var(--primary);\n" +
                "            margin-bottom: 1rem;\n" +
                "            font-weight: 500;\n" +
                "        }\n" +
                "        .summary-item p {\n" +
                "            color: var(--text);\n" +
                "            line-height: 1.8;\n" +
                "            margin-bottom: 1rem;\n" +
                "        }\n" +
                "        .highlight {\n" +
                "            color: var(--accent);\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        footer {\n" +
                "            text-align: center;\n" +
                "            margin-top: 3rem;\n" +
                "            padding: 1.5rem;\n" +
                "            color: var(--text-light);\n" +
                "            font-size: 0.9rem;\n" +
                "            border-top: 1px solid var(--border);\n" +
                "        }\n" +
                "        .watermark {\n" +
                "            position: fixed;\n" +
                "            bottom: 20px;\n" +
                "            right: 20px;\n" +
                "            opacity: 0.1;\n" +
                "            font-size: 1rem;\n" +
                "            transform: rotate(-45deg);\n" +
                "            pointer-events: none;\n" +
                "            z-index: 1000;\n" +
                "        }\n" +
                "        @media print {\n" +
                "            body {\n" +
                "                background-color: white;\n" +
                "            }\n" +
                "            .container {\n" +
                "                padding: 0;\n" +
                "                max-width: 100%;\n" +
                "            }\n" +
                "            .card, .stat-card, .chart-card, .summary-section {\n" +
                "                box-shadow: none;\n" +
                "                border: 1px solid var(--border);\n" +
                "            }\n" +
                "            .card:hover, .stat-card:hover, .chart-card:hover {\n" +
                "                transform: none;\n" +
                "                box-shadow: none;\n" +
                "            }\n" +
                "            .watermark {\n" +
                "                opacity: 0.3;\n" +
                "            }\n" +
                "        }\n" +
                "        @media (max-width: 768px) {\n" +
                "            .chart-container {\n" +
                "                grid-template-columns: 1fr;\n" +
                "            }\n" +
                "            .stats-container {\n" +
                "                grid-template-columns: repeat(2, 1fr);\n" +
                "            }\n" +
                "            .container {\n" +
                "                padding: 1.5rem;\n" +
                "            }\n" +
                "            header {\n" +
                "                padding: 2rem 1rem;\n" +
                "            }\n" +
                "            h1 {\n" +
                "                font-size: 2rem;\n" +
                "            }\n" +
                "            .student-info div {\n" +
                "                min-width: 120px;\n" +
                "                padding: 0.8rem 1rem;\n" +
                "            }\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <header>\n" +
                "            <h1>学生练习报告</h1>\n" +
                "            <p>" + user.getName() + " (" + user.getUsername() + ")</p>\n" +
                "            <div class=\"student-info\">\n" +
                "                <div>\n" +
                "                    <h3>学号</h3>\n" +
                "                    <p>" + user.getUser_id() + "</p>\n" +
                "                </div>\n" +
                "                <div>\n" +
                "                    <h3>邮箱</h3>\n" +
                "                    <p>" + user.getMail() + "</p>\n" +
                "                </div>\n" +
                "                <div>\n" +
                "                    <h3>生成日期</h3>\n" +
                "                    <p>" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "</p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </header>\n" +
                "\n" +
                "        <div class=\"stats-container\">\n" +
                "            <div class=\"stat-card\">\n" +
                "                <h3>总练习数</h3>\n" +
                "                <p>" + totalExercises + "</p>\n" +
                "                <div class=\"trend\">完成情况良好</div>\n" +
                "            </div>\n" +
                "            <div class=\"stat-card\">\n" +
                "                <h3>平均分数</h3>\n" +
                "                <p>" + String.format("%.1f", averageScore) + "</p>\n" +
                "                <div class=\"trend " + (isScoreImproving ? "up" : "down") + "\">\n" +
                "                    " + (isScoreImproving ? "呈上升趋势" : "需要加强") + "\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"stat-card\">\n" +
                "                <h3>最高排名</h3>\n" +
                "                <p>" + highestRank + "</p>\n" +
                "                <div class=\"trend\">超过" + String.format("%.1f", (1 - (double)highestRank/lowestRank) * 100) + "%的同学</div>\n" +
                "            </div>\n" +
                "            <div class=\"stat-card\">\n" +
                "                <h3>最低排名</h3>\n" +
                "                <p>" + lowestRank + "</p>\n" +
                "                <div class=\"trend\">仍有提升空间</div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"summary-section\">\n" +
                "            <h2>学习概要</h2>\n" +
                "            <div class=\"summary-content\">\n" +
                "                <div class=\"summary-item\">\n" +
                "                    <h3>成绩表现</h3>\n" +
                "                    <p>在所有练习中，您的平均分为 <span class=\"highlight\">" + String.format("%.1f", averageScore) + "</span>，\n" +
                "                    最高分为 <span class=\"highlight\">" + highestScore + "</span>，\n" +
                "                    最低分为 <span class=\"highlight\">" + lowestScore + "</span>。\n" +
                "                    " + (isScoreImproving ? "您的成绩呈现上升趋势，请继续保持！" : "您的成绩有波") + "\n" +
                "                    " + (isScoreImproving ? "您的成绩呈现上升趋势，请继续保持！" : "您的成绩有波动，建议加强薄弱环节的练习。") + "\n" +
                "                    </p>\n" +
                "                </div>\n" +
                "                <div class=\"summary-item\">\n" +
                "                    <h3>排名情况</h3>\n" +
                "                    <p>您的最高排名为 <span class=\"highlight\">" + highestRank + "</span>，\n" +
                "                    平均排名为 <span class=\"highlight\">" + String.format("%.1f", averageRank) + "</span>。\n" +
                "                    在班级中的表现 <span class=\"highlight\">" + (highestRank <= 10 ? "优秀" : (highestRank <= 20 ? "良好" : "中等")) + "</span>。\n" +
                "                    继续努力，争取更高排名！</p>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"chart-container\">\n" +
                "            <div class=\"chart-card\">\n" +
                "                <h2>成绩趋势</h2>\n" +
                "                <canvas id=\"scoreChart\"></canvas>\n" +
                "            </div>\n" +
                "            <div class=\"chart-card\">\n" +
                "                <h2>排名变化</h2>\n" +
                "                <canvas id=\"rankChart\"></canvas>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div class=\"card\">\n" +
                "            <h2>练习详情</h2>\n" +
                "            <table>\n" +
                "                <thead>\n" +
                "                    <tr>\n" +
                "                        <th>练习名称</th>\n" +
                "                        <th>分数</th>\n" +
                "                        <th>排名</th>\n" +
                "                        <th>时间范围</th>\n" +
                "                    </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                    " + exerciseTable.toString() + "\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "\n" +
                "        <footer>\n" +
                "            <p>© " + new SimpleDateFormat("yyyy").format(new Date()) + " 学习报告系统 | 生成时间: " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "</p>\n" +
                "        </footer>\n" +
                "        <div class=\"watermark\">" + user.getName() + " - " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "</div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script>\n" +
                "        // 成绩趋势图\n" +
                "        const scoreCtx = document.getElementById('scoreChart').getContext('2d');\n" +
                "        const scoreChart = new Chart(scoreCtx, {\n" +
                "            type: 'line',\n" +
                "            data: {\n" +
                "                labels: [" + exerciseNames + "],\n" +
                "                datasets: [{\n" +
                "                    label: '分数',\n" +
                "                    data: [" + scoreData + "],\n" +
                "                    backgroundColor: 'rgba(156, 39, 176, 0.2)',\n" +
                "                    borderColor: 'rgba(156, 39, 176, 1)',\n" +
                "                    borderWidth: 3,\n" +
                "                    tension: 0.4,\n" +
                "                    pointBackgroundColor: 'rgba(156, 39, 176, 1)',\n" +
                "                    pointRadius: 6,\n" +
                "                    pointHoverRadius: 8,\n" +
                "                    fill: true\n" +
                "                }]\n" +
                "            },\n" +
                "            options: {\n" +
                "                responsive: true,\n" +
                "                plugins: {\n" +
                "                    legend: {\n" +
                "                        position: 'top',\n" +
                "                    },\n" +
                "                    tooltip: {\n" +
                "                        mode: 'index',\n" +
                "                        intersect: false,\n" +
                "                        backgroundColor: 'rgba(255, 255, 255, 0.9)',\n" +
                "                        titleColor: '#6a5acd',\n" +
                "                        bodyColor: '#2c2c2c',\n" +
                "                        borderColor: '#e0e0e0',\n" +
                "                        borderWidth: 1,\n" +
                "                        cornerRadius: 8,\n" +
                "                        padding: 12,\n" +
                "                        boxPadding: 6,\n" +
                "                        usePointStyle: true\n" +
                "                    }\n" +
                "                },\n" +
                "                scales: {\n" +
                "                    y: {\n" +
                "                        beginAtZero: true,\n" +
                "                        grid: {\n" +
                "                            color: 'rgba(0, 0, 0, 0.05)'\n" +
                "                        },\n" +
                "                        ticks: {\n" +
                "                            font: {\n" +
                "                                family: 'Poppins'\n" +
                "                            }\n" +
                "                        },\n" +
                "                        title: {\n" +
                "                            display: true,\n" +
                "                            text: '分数',\n" +
                "                            color: '#6c6c6c',\n" +
                "                            font: {\n" +
                "                                family: 'Poppins',\n" +
                "                                size: 13,\n" +
                "                                weight: 500\n" +
                "                            }\n" +
                "                        }\n" +
                "                    },\n" +
                "                    x: {\n" +
                "                        grid: {\n" +
                "                            display: false\n" +
                "                        },\n" +
                "                        ticks: {\n" +
                "                            font: {\n" +
                "                                family: 'Poppins'\n" +
                "                            },\n" +
                "                            maxRotation: 45,\n" +
                "                            minRotation: 45\n" +
                "                        },\n" +
                "                        title: {\n" +
                "                            display: true,\n" +
                "                            text: '练习',\n" +
                "                            color: '#6c6c6c',\n" +
                "                            font: {\n" +
                "                                family: 'Poppins',\n" +
                "                                size: 13,\n" +
                "                                weight: 500\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        // 排名变化图\n" +
                "        const rankCtx = document.getElementById('rankChart').getContext('2d');\n" +
                "        const rankChart = new Chart(rankCtx, {\n" +
                "            type: 'line',\n" +
                "            data: {\n" +
                "                labels: [" + exerciseNames + "],\n" +
                "                datasets: [{\n" +
                "                    label: '排名',\n" +
                "                    data: [" + rankingData + "],\n" +
                "                    backgroundColor: 'rgba(106, 90, 205, 0.2)',\n" +
                "                    borderColor: 'rgba(106, 90, 205, 1)',\n" +
                "                    borderWidth: 3,\n" +
                "                    tension: 0.4,\n" +
                "                    pointBackgroundColor: 'rgba(106, 90, 205, 1)',\n" +
                "                    pointRadius: 6,\n" +
                "                    pointHoverRadius: 8,\n" +
                "                    fill: true\n" +
                "                }]\n" +
                "            },\n" +
                "            options: {\n" +
                "                responsive: true,\n" +
                "                plugins: {\n" +
                "                    legend: {\n" +
                "                        position: 'top',\n" +
                "                    },\n" +
                "                    tooltip: {\n" +
                "                        mode: 'index',\n" +
                "                        intersect: false,\n" +
                "                        backgroundColor: 'rgba(255, 255, 255, 0.9)',\n" +
                "                        titleColor: '#6a5acd',\n" +
                "                        bodyColor: '#2c2c2c',\n" +
                "                        borderColor: '#e0e0e0',\n" +
                "                        borderWidth: 1,\n" +
                "                        cornerRadius: 8,\n" +
                "                        padding: 12,\n" +
                "                        boxPadding: 6,\n" +
                "                        usePointStyle: true\n" +
                "                    }\n" +
                "                },\n" +
                "                scales: {\n" +
                "                    y: {\n" +
                "                        reverse: true,\n" +
                "                        grid: {\n" +
                "                            color: 'rgba(0, 0, 0, 0.05)'\n" +
                "                        },\n" +
                "                        ticks: {\n" +
                "                            font: {\n" +
                "                                family: 'Poppins'\n" +
                "                            }\n" +
                "                        },\n" +
                "                        title: {\n" +
                "                            display: true,\n" +
                "                            text: '排名',\n" +
                "                            color: '#6c6c6c',\n" +
                "                            font: {\n" +
                "                                family: 'Poppins',\n" +
                "                                size: 13,\n" +
                "                                weight: 500\n" +
                "                            }\n" +
                "                        }\n" +
                "                    },\n" +
                "                    x: {\n" +
                "                        grid: {\n" +
                "                            display: false\n" +
                "                        },\n" +
                "                        ticks: {\n" +
                "                            font: {\n" +
                "                                family: 'Poppins'\n" +
                "                            },\n" +
                "                            maxRotation: 45,\n" +
                "                            minRotation: 45\n" +
                "                        },\n" +
                "                        title: {\n" +
                "                            display: true,\n" +
                "                            text: '练习',\n" +
                "                            color: '#6c6c6c',\n" +
                "                            font: {\n" +
                "                                family: 'Poppins',\n" +
                "                                size: 13,\n" +
                "                                weight: 500\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        String fileName = "练习报告_" + user.getUser_id() + ".html";

        Files.createDirectories(Paths.get("reports"));
        String filePath = "reports/" + fileName;

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(htmlContent);
        }

        return filePath;
    }
}