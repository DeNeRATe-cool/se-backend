package com.se.utils;

import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.entity.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 练习反馈报告生成工具类
 * 提供精美的HTML报告生成功能
 */
public class ExerciseFeedbackReportGenerator {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 生成练习反馈报告
     * @param exercise 练习信息
     * @param students 参与学生列表
     * @param scores 学生得分列表
     * @param problems 题目列表
     * @param ratios 每道题通过率
     * @param averages 每道题平均分
     * @return 生成的HTML文件路径
     */
    public static String generateReport(
            Exercise exercise,
            List<User> students,
            List<Integer> scores,
            List<Problem> problems,
            List<Float> ratios,
            List<Float> averages) throws IOException {

        // 过滤掉负分的学生
        List<User> validStudents = new ArrayList<>();
        List<Integer> validScores = new ArrayList<>();

        for (int i = 0; i < students.size(); i++) {
            if (i < scores.size() && scores.get(i) >= 0) {
                validStudents.add(students.get(i));
                validScores.add(scores.get(i));
            }
        }

        // 计算额外的统计数据
        Map<String, Object> stats = calculateStatistics(validStudents, validScores, problems, ratios, averages);

        // 生成HTML内容
        String htmlContent = generatePremiumDashboardStyle(exercise, validStudents, validScores, problems, ratios, averages, stats);

        // 确保目录存在
        File directory = new File("./reports");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 写入文件
        String fileName = "./reports/反馈报告_" + exercise.getExer_id() + ".html";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(htmlContent);
        }

        return fileName;
    }

    /**
     * 计算额外的统计数据
     */
    private static Map<String, Object> calculateStatistics(
            List<User> students,
            List<Integer> scores,
            List<Problem> problems,
            List<Float> ratios,
            List<Float> averages) {

        Map<String, Object> stats = new HashMap<>();

        // 过滤掉负分学生
        List<Integer> validScores = new ArrayList<>();
        List<User> validStudents = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i) >= 0) {
                validScores.add(scores.get(i));
                validStudents.add(students.get(i));
            }
        }

        // 使用过滤后的列表进行统计
        int totalStudents = validStudents.size();
        int totalProblems = problems.size();

        // 计算分数统计
        int maxScore = validScores.isEmpty() ? 0 : Collections.max(validScores);
        int minScore = validScores.isEmpty() ? 0 : Collections.min(validScores);
        double avgScore = validScores.stream().mapToInt(Integer::intValue).average().orElse(0.0);

        // 计算分数分布
        Map<String, Integer> scoreDistribution = new HashMap<>();
        scoreDistribution.put("优秀(90-100)", 0);
        scoreDistribution.put("良好(80-89)", 0);
        scoreDistribution.put("中等(70-79)", 0);
        scoreDistribution.put("及格(60-69)", 0);
        scoreDistribution.put("不及格(0-59)", 0);

        for (Integer score : scores) {
            if (score >= 90) {
                scoreDistribution.put("优秀(90-100)", scoreDistribution.get("优秀(90-100)") + 1);
            } else if (score >= 80) {
                scoreDistribution.put("良好(80-89)", scoreDistribution.get("良好(80-89)") + 1);
            } else if (score >= 70) {
                scoreDistribution.put("中等(70-79)", scoreDistribution.get("中等(70-79)") + 1);
            } else if (score >= 60) {
                scoreDistribution.put("及格(60-69)", scoreDistribution.get("及格(60-69)") + 1);
            } else {
                scoreDistribution.put("不及格(0-59)", scoreDistribution.get("不及格(0-59)") + 1);
            }
        }

        // 计算题目难度分布
        Map<String, Integer> difficultyDistribution = new HashMap<>();
        difficultyDistribution.put("简单(通过率>80%)", 0);
        difficultyDistribution.put("中等(通过率40%-80%)", 0);
        difficultyDistribution.put("困难(通过率<40%)", 0);

        for (Float ratio : ratios) {
            if (ratio > 0.8) {
                difficultyDistribution.put("简单(通过率>80%)", difficultyDistribution.get("简单(通过率>80%)") + 1);
            } else if (ratio >= 0.4) {
                difficultyDistribution.put("中等(通过率40%-80%)", difficultyDistribution.get("中等(通过率40%-80%)") + 1);
            } else {
                difficultyDistribution.put("困难(通过率<40%)", difficultyDistribution.get("困难(通过率<40%)") + 1);
            }
        }

        // 找出最难和最简单的题目
        int hardestProblemIndex = 0;
        int easiestProblemIndex = 0;

        if (!ratios.isEmpty()) {
            float minRatio = ratios.get(0);
            float maxRatio = ratios.get(0);

            for (int i = 1; i < ratios.size(); i++) {
                if (ratios.get(i) < minRatio) {
                    minRatio = ratios.get(i);
                    hardestProblemIndex = i;
                }
                if (ratios.get(i) > maxRatio) {
                    maxRatio = ratios.get(i);
                    easiestProblemIndex = i;
                }
            }
        }

        // 计算排名
        List<Integer> rankScores = new ArrayList<>(scores);
        Collections.sort(rankScores, Collections.reverseOrder());

        List<Integer> rankings = new ArrayList<>();
        for (Integer score : scores) {
            rankings.add(rankScores.indexOf(score) + 1);
        }

        // 计算标准差
        double mean = avgScore;
        double variance = scores.stream()
                .mapToDouble(score -> Math.pow(score - mean, 2))
                .average()
                .orElse(0.0);
        double stdDev = Math.sqrt(variance);

        // 存储计算结果
        stats.put("totalStudents", totalStudents);
        stats.put("totalProblems", totalProblems);
        stats.put("maxScore", maxScore);
        stats.put("minScore", minScore);
        stats.put("avgScore", avgScore);
        stats.put("stdDev", stdDev);
        stats.put("scoreDistribution", scoreDistribution);
        stats.put("difficultyDistribution", difficultyDistribution);
        stats.put("hardestProblemIndex", hardestProblemIndex);
        stats.put("easiestProblemIndex", easiestProblemIndex);
        stats.put("rankings", rankings);

        // 计算前5名和后5名学生
        List<Integer> studentIndices = IntStream.range(0, scores.size()).boxed()
                .sorted(Comparator.comparing(scores::get).reversed())
                .collect(Collectors.toList());

        List<Integer> topIndices = studentIndices.subList(0, Math.min(5, studentIndices.size()));
        List<Integer> bottomIndices = studentIndices.subList(
                Math.max(0, studentIndices.size() - 5), studentIndices.size());
        Collections.reverse(bottomIndices);

        stats.put("topIndices", topIndices);
        stats.put("bottomIndices", bottomIndices);

        return stats;
    }

    /**
     * 生成高级仪表盘风格的报告
     */
    private static String generatePremiumDashboardStyle(
            Exercise exercise,
            List<User> students,
            List<Integer> scores,
            List<Problem> problems,
            List<Float> ratios,
            List<Float> averages,
            Map<String, Object> stats) {

        StringBuilder html = new StringBuilder();

        // 报告头部
        html.append("<!DOCTYPE html>\n")
                .append("<html lang=\"zh-CN\">\n")
                .append("<head>\n")
                .append("    <meta charset=\"UTF-8\">\n")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                .append("    <title>练习反馈报告 - ").append(exercise.getName()).append("</title>\n")
                .append("    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\n")
                .append("    <script src=\"https://cdn.jsdelivr.net/npm/chart.js\"></script>\n")
                .append("    <style>\n")
                .append("        :root {\n")
                .append("            --primary-color: #6a5acd;\n")
                .append("            --secondary-color: #483d8b;\n")
                .append("            --accent-color: #9370db;\n")
                .append("            --light-accent: #e6e6fa;\n")
                .append("            --background-color: #f8f9fa;\n")
                .append("            --card-color: #ffffff;\n")
                .append("            --text-color: #333333;\n")
                .append("            --border-radius: 12px;\n")
                .append("            --box-shadow: 0 8px 30px rgba(0, 0, 0, 0.08);\n")
                .append("        }\n")
                .append("        body {\n")
                .append("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n")
                .append("            background-color: var(--background-color);\n")
                .append("            color: var(--text-color);\n")
                .append("            padding-top: 2rem;\n")
                .append("            padding-bottom: 2rem;\n")
                .append("        }\n")
                .append("        .report-header {\n")
                .append("            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));\n")
                .append("            color: white;\n")
                .append("            padding: 2.5rem;\n")
                .append("            border-radius: var(--border-radius);\n")
                .append("            margin-bottom: 2.5rem;\n")
                .append("            box-shadow: var(--box-shadow);\n")
                .append("            position: relative;\n")
                .append("            overflow: hidden;\n")
                .append("        }\n")
                .append("        .report-header::before {\n")
                .append("            content: '';\n")
                .append("            position: absolute;\n")
                .append("            top: -50%;\n")
                .append("            right: -50%;\n")
                .append("            width: 100%;\n")
                .append("            height: 200%;\n")
                .append("            background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);\n")
                .append("            z-index: 0;\n")
                .append("        }\n")
                .append("        .report-header .content {\n")
                .append("            position: relative;\n")
                .append("            z-index: 1;\n")
                .append("        }\n")
                .append("        .report-title {\n")
                .append("            font-weight: 700;\n")
                .append("            margin-bottom: 0.5rem;\n")
                .append("            font-size: 2.2rem;\n")
                .append("            border-bottom: 2px solid var(--accent-color);\n")
                .append("            padding-bottom: 0.5rem;\n")
                .append("            display: inline-block;\n")
                .append("        }\n")
                .append("        .card {\n")
                .append("            border-radius: var(--border-radius);\n")
                .append("            box-shadow: var(--box-shadow);\n")
                .append("            margin-bottom: 2rem;\n")
                .append("            border: none;\n")
                .append("            transition: transform 0.3s ease, box-shadow 0.3s ease;\n")
                .append("            overflow: hidden;\n")
                .append("        }\n")
                .append("        .card:hover {\n")
                .append("            transform: translateY(-5px);\n")
                .append("            box-shadow: 0 15px 35px rgba(123, 104, 238, 0.15);\n")
                .append("        }\n")
                .append("        .card-header {\n")
                .append("            background-color: var(--primary-color);\n")
                .append("            color: white;\n")
                .append("            border-top-left-radius: var(--border-radius) !important;\n")
                .append("            border-top-right-radius: var(--border-radius) !important;\n")
                .append("            font-weight: 600;\n")
                .append("            padding: 1.2rem 1.5rem;\n")
                .append("            border-bottom: 3px solid var(--accent-color);\n")
                .append("        }\n")
                .append("        .card-body {\n")
                .append("            padding: 1.8rem;\n")
                .append("        }\n")
                .append("        .stat-card {\n")
                .append("            text-align: center;\n")
                .append("            padding: 1.8rem;\n")
                .append("            height: 100%;\n")
                .append("            display: flex;\n")
                .append("            flex-direction: column;\n")
                .append("            justify-content: center;\n")
                .append("            align-items: center;\n")
                .append("            background: linear-gradient(to bottom, #ffffff, #f9f9ff);\n")
                .append("        }\n")
                .append("        .stat-icon {\n")
                .append("            width: 70px;\n")
                .append("            height: 70px;\n")
                .append("            border-radius: 50%;\n")
                .append("            background-color: var(--light-accent);\n")
                .append("            display: flex;\n")
                .append("            justify-content: center;\n")
                .append("            align-items: center;\n")
                .append("            margin-bottom: 1.2rem;\n")
                .append("            position: relative;\n")
                .append("            box-shadow: 0 5px 15px rgba(123, 104, 238, 0.15);\n")
                .append("        }\n")
                .append("        .stat-icon::before {\n")
                .append("            content: '';\n")
                .append("            position: absolute;\n")
                .append("            width: 100%;\n")
                .append("            height: 100%;\n")
                .append("            border-radius: 50%;\n")
                .append("            border: 2px solid var(--accent-color);\n")
                .append("            opacity: 0.3;\n")
                .append("            animation: pulse 2s infinite;\n")
                .append("        }\n")
                .append("        @keyframes pulse {\n")
                .append("            0% { transform: scale(1); opacity: 0.3; }\n")
                .append("            50% { transform: scale(1.1); opacity: 0.1; }\n")
                .append("            100% { transform: scale(1); opacity: 0.3; }\n")
                .append("        }\n")
                .append("        .stat-icon svg {\n")
                .append("            width: 30px;\n")
                .append("            height: 30px;\n")
                .append("            fill: var(--primary-color);\n")
                .append("        }\n")
                .append("        .stat-value {\n")
                .append("            font-size: 2.5rem;\n")
                .append("            font-weight: 700;\n")
                .append("            color: var(--primary-color);\n")
                .append("            margin-bottom: 0.5rem;\n")
                .append("        }\n")
                .append("        .stat-label {\n")
                .append("            font-size: 1rem;\n")
                .append("            color: #666;\n")
                .append("            font-weight: 500;\n")
                .append("        }\n")
                .append("        .table-responsive {\n")
                .append("            border-radius: var(--border-radius);\n")
                .append("            overflow: hidden;\n")
                .append("        }\n")
                .append("        .table {\n")
                .append("            margin-bottom: 0;\n")
                .append("        }\n")
                .append("        .table thead th {\n")
                .append("            background-color: #f2f2ff;\n")
                .append("            border-bottom: 2px solid #e0e0fa;\n")
                .append("            font-weight: 600;\n")
                .append("            color: var(--primary-color);\n")
                .append("        }\n")
                .append("        .table tbody tr:hover {\n")
                .append("            background-color: rgba(123, 104, 238, 0.05);\n")
                .append("        }\n")
                .append("        .badge-success {\n")
                .append("            background-color: #6a5acd;\n")
                .append("        }\n")
                .append("        .badge-warning {\n")
                .append("            background-color: #dda0dd;\n")
                .append("            color: #333;\n")
                .append("        }\n")
                .append("        .badge-danger {\n")
                .append("            background-color: #9932cc;\n")
                .append("        }\n")
                .append("        .progress {\n")
                .append("            height: 10px;\n")
                .append("            border-radius: 5px;\n")
                .append("            background-color: #e9ecef;\n")
                .append("            overflow: hidden;\n")
                .append("        }\n")
                .append("        .progress-bar {\n")
                .append("            background-color: var(--primary-color);\n")
                .append("        }\n")
                .append("        .footer {\n")
                .append("            text-align: center;\n")
                .append("            margin-top: 3rem;\n")
                .append("            padding: 1.5rem;\n")
                .append("            background-color: var(--card-color);\n")
                .append("            border-radius: var(--border-radius);\n")
                .append("            box-shadow: var(--box-shadow);\n")
                .append("            color: #666;\n")
                .append("        }\n")
                .append("        .chart-container {\n")
                .append("            position: relative;\n")
                .append("            height: 300px;\n")
                .append("            width: 100%;\n")
                .append("        }\n")
                .append("        .problem-detail {\n")
                .append("            background-color: #f9f9ff;\n")
                .append("            border-radius: 8px;\n")
                .append("            padding: 1.2rem;\n")
                .append("            margin-top: 1rem;\n")
                .append("            border-left: 4px solid var(--primary-color);\n")
                .append("        }\n")
                .append("        .problem-detail h5 {\n")
                .append("            color: var(--primary-color);\n")
                .append("            font-weight: 600;\n")
                .append("            margin-bottom: 1rem;\n")
                .append("        }\n")
                .append("        .student-info {\n")
                .append("            display: flex;\n")
                .append("            align-items: center;\n")
                .append("        }\n")
                .append("        .student-avatar {\n")
                .append("            width: 40px;\n")
                .append("            height: 40px;\n")
                .append("            border-radius: 50%;\n")
                .append("            background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));\n")
                .append("            color: white;\n")
                .append("            display: flex;\n")
                .append("            justify-content: center;\n")
                .append("            align-items: center;\n")
                .append("            font-weight: 600;\n")
                .append("            margin-right: 10px;\n")
                .append("            box-shadow: 0 3px 8px rgba(123, 104, 238, 0.2);\n")
                .append("        }\n")
                .append("        .student-name {\n")
                .append("            font-weight: 600;\n")
                .append("        }\n")
                .append("        .student-id {\n")
                .append("            font-size: 0.85rem;\n")
                .append("            color: #666;\n")
                .append("        }\n")
                .append("        .highlight {\n")
                .append("            color: var(--primary-color);\n")
                .append("            font-weight: 600;\n")
                .append("        }\n")
                .append("        .list-group-item {\n")
                .append("            border-left: 3px solid var(--accent-color);\n")
                .append("            margin-bottom: 0.5rem;\n")
                .append("            border-radius: 4px;\n")
                .append("            transition: all 0.2s ease;\n")
                .append("        }\n")
                .append("        .list-group-item:hover {\n")
                .append("            background-color: var(--light-accent);\n")
                .append("            transform: translateX(5px);\n")
                .append("        }\n")
                .append("    </style>\n")
                .append("</head>\n")
                .append("<body>\n")
                .append("    <div class=\"container\">\n");

        // 报告标题
        html.append("        <div class=\"report-header\">\n")
                .append("            <div class=\"content\">\n")
                .append("                <div class=\"row align-items-center\">\n")
                .append("                    <div class=\"col-md-8\">\n")
                .append("                        <h1 class=\"report-title\">").append(exercise.getName()).append("</h1>\n")
                .append("                        <p class=\"lead mb-0\">练习反馈分析报告 | 生成时间: ").append(dateFormat.format(new Date())).append("</p>\n")
                .append("                    </div>\n")
                .append("                    <div class=\"col-md-4 text-md-end\">\n")
                .append("                        <p class=\"mb-0\"><strong>练习ID:</strong> ").append(exercise.getExer_id()).append("</p>\n")
                .append("                        <p class=\"mb-0\"><strong>课程ID:</strong> ").append(exercise.getCourse_id()).append("</p>\n")
                .append("                        <p class=\"mb-0\"><strong>开始时间:</strong> ").append(dateFormat.format(exercise.getBegin_time())).append("</p>\n")
                .append("                        <p class=\"mb-0\"><strong>结束时间:</strong> ").append(dateFormat.format(exercise.getEnd_time())).append("</p>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 统计概览
        html.append("        <div class=\"row\">\n")
                .append("            <div class=\"col-md-3 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"stat-card\">\n")
                .append("    <div class=\"stat-icon\">\n")
                .append("        <svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n")
                .append("            <path d=\"M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2\"></path>\n")
                .append("            <circle cx=\"9\" cy=\"7\" r=\"4\"></circle>\n")
                .append("            <path d=\"M23 21v-2a4 4 0 0 0-3-3.87\"></path>\n")
                .append("            <path d=\"M16 3.13a4 4 0 0 1 0 7.75\"></path>\n")
                .append("        </svg>\n")
                .append("    </div>\n")
                .append("                        <div class=\"stat-value\">").append(stats.get("totalStudents")).append("</div>\n")
                .append("                        <div class=\"stat-label\">参与学生数</div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("            <div class=\"col-md-3 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"stat-card\">\n")
                .append("                        <div class=\"stat-icon\">\n")
                .append("                            <svg width=\"24" +
                        "\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n" +
                        "                                <rect x=\"3\" y=\"4\" width=\"18\" height=\"18\" rx=\"2\" ry=\"2\"></rect>\n" +
                        "                                <line x1=\"16\" y1=\"2\" x2=\"16\" y2=\"6\"></line>\n" +
                        "                                <line x1=\"8\" y1=\"2\" x2=\"8\" y2=\"6\"></line>\n" +
                        "                                <line x1=\"3\" y1=\"10\" x2=\"21\" y2=\"10\"></line>\n" +
                        "                            </svg>\n")
                .append("                        </div>\n")
                .append("                        <div class=\"stat-value\">").append(stats.get("totalProblems")).append("</div>\n")
                .append("                        <div class=\"stat-label\">题目数量</div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("            <div class=\"col-md-3 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"stat-card\">\n")
                .append("                        <div class=\"stat-icon\">\n")
                .append("                            <svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n" +
                        "                                <circle cx=\"12\" cy=\"12\" r=\"10\"></circle>\n" +
                        "                                <polyline points=\"16.24 7.76 14.12 14.12 7.76 16.24\"></polyline>\n" +
                        "                                <line x1=\"12\" y1=\"2\" x2=\"12\" y2=\"12\"></line>\n" +
                        "                                <line x1=\"2\" y1=\"12\" x2=\"12\" y2=\"12\"></line>\n" +
                        "                            </svg>\n")
                .append("                        </div>\n")
                .append("                        <div class=\"stat-value\">").append(df.format(stats.get("avgScore"))).append("</div>\n")
                .append("                        <div class=\"stat-label\">平均分</div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("            <div class=\"col-md-3 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"stat-card\">\n")
                .append("                        <div class=\"stat-icon\">\n")
                .append("                            <svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\">\n" +
                        "                                <line x1=\"18\" y1=\"20\" x2=\"6\" y2=\"20\"></line>\n" +
                        "                                <line x1=\"12\" y1=\"4\" x2=\"12\" y2=\"20\"></line>\n" +
                        "                                <path d=\"M4 14h.01\"></path>\n" +
                        "                                <path d=\"M4 18h.01\"></path>\n" +
                        "                                <path d=\"M4 22h.01\"></path>\n" +
                        "                                <path d=\"M20 14h.01\"></path>\n" +
                        "                                <path d=\"M20 18h.01\"></path>\n" +
                        "                                <path d=\"M20 22h.01\"></path>\n" +
                        "                            </svg>\n")
                .append("                        </div>\n")
                .append("                        <div class=\"stat-value\">").append(df.format(stats.get("stdDev"))).append("</div>\n")
                .append("                        <div class=\"stat-label\">标准差</div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 分数分布图表
        html.append("        <div class=\"row\">\n")
                .append("            <div class=\"col-md-6 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"card-header\">分数分布</div>\n")
                .append("                    <div class=\"card-body\">\n")
                .append("                        <div class=\"chart-container\">\n")
                .append("                            <canvas id=\"scoreDistributionChart\"></canvas>\n")
                .append("                        </div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("            <div class=\"col-md-6 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"card-header\">题目难度分布</div>\n")
                .append("                    <div class=\"card-body\">\n")
                .append("                        <div class=\"chart-container\">\n")
                .append("                            <canvas id=\"difficultyDistributionChart\"></canvas>\n")
                .append("                        </div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 题目分析
        html.append("        <div class=\"card\">\n")
                .append("            <div class=\"card-header\">题目分析</div>\n")
                .append("            <div class=\"card-body\">\n")
                .append("                <div class=\"table-responsive\">\n")
                .append("                    <table class=\"table table-striped\">\n")
                .append("                        <thead>\n")
                .append("                            <tr>\n")
                .append("                                <th>题号</th>\n")
                .append("                                <th>题目描述</th>\n")
                .append("                                <th>通过率</th>\n")
                .append("                                <th>平均分</th>\n")
                .append("                                <th>难度</th>\n")
                .append("                            </tr>\n")
                .append("                        </thead>\n")
                .append("                        <tbody>\n");

        for (int i = 0; i < problems.size(); i++) {
            Problem problem = problems.get(i);
            float ratio = ratios.get(i);
            float average = averages.get(i);

            String difficultyBadge;
            if (ratio > 0.8) {
                difficultyBadge = "<span class=\"badge bg-success\">简单</span>";
            } else if (ratio >= 0.4) {
                difficultyBadge = "<span class=\"badge bg-warning\">中等</span>";
            } else {
                difficultyBadge = "<span class=\"badge bg-danger\">困难</span>";
            }

            html.append("                            <tr>\n")
                    .append("                                <td>").append(i + 1).append("</td>\n")
                    .append("                                <td>").append(problem.getDescription().length() > 50 ? problem.getDescription().substring(0, 50) + "..." : problem.getDescription()).append("</td>\n")
                    .append("                                <td>\n")
                    .append("                                    <div class=\"progress\">\n")
                    .append("                                        <div class=\"progress-bar\" role=\"progressbar\" style=\"width: ").append(ratio * 100).append("%\" aria-valuenow=\"").append(ratio * 100).append("\" aria-valuemin=\"0\" aria-valuemax=\"100\">").append(df.format(ratio * 100)).append("%</div>\n")
                    .append("                                    </div>\n")
                    .append("                                </td>\n")
                    .append("                                <td>").append(df.format(average)).append("</td>\n")
                    .append("                                <td>").append(difficultyBadge).append("</td>\n")
                    .append("                            </tr>\n");
        }

        html.append("                        </tbody>\n")
                .append("                    </table>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 题目通过率图表
        html.append("        <div class=\"card\">\n")
                .append("            <div class=\"card-header\">题目通过率与平均分对比</div>\n")
                .append("            <div class=\"card-body\">\n")
                .append("                <div class=\"chart-container\">\n")
                .append("                    <canvas id=\"problemRatiosChart\"></canvas>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 学生表现
        html.append("        <div class=\"row\">\n")
                .append("            <div class=\"col-md-6 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"card-header\">成绩最高的学生</div>\n")
                .append("                    <div class=\"card-body\">\n")
                .append("                        <div class=\"table-responsive\">\n")
                .append("                            <table class=\"table table-hover\">\n")
                .append("                                <thead>\n")
                .append("                                    <tr>\n")
                .append("                                        <th>排名</th>\n")
                .append("                                        <th>学生信息</th>\n")
                .append("                                        <th>分数</th>\n")
                .append("                                    </tr>\n")
                .append("                                </thead>\n")
                .append("                                <tbody>\n");

        @SuppressWarnings("unchecked")
        List<Integer> topIndices = (List<Integer>) stats.get("topIndices");
        for (int i = 0; i < topIndices.size(); i++) {
            int index = topIndices.get(i);
            User student = students.get(index);
            int score = scores.get(index);

            String initials = "";
            if (student.getName() != null && !student.getName().isEmpty()) {
                String[] nameParts = student.getName().split(" ");
                for (String part : nameParts) {
                    if (!part.isEmpty()) {
                        initials += part.charAt(0);
                    }
                }
            } else if (student.getUsername() != null && !student.getUsername().isEmpty()) {
                initials = student.getUsername().substring(0, Math.min(2, student.getUsername().length()));
            } else {
                initials = "U" + student.getUser_id();
            }

            html.append("                                    <tr>\n")
                    .append("                                        <td><span class=\"badge bg-success\">").append(i + 1).append("</span></td>\n")
                    .append("                                        <td>\n")
                    .append("                                            <div class=\"student-info\">\n")
                    .append("                                                <div class=\"student-avatar\">").append(initials).append("</div>\n")
                    .append("                                                <div>\n")
                    .append("                                                    <div class=\"student-name\">").append(student.getName() != null ? student.getName() : student.getUsername()).append("</div>\n")
                    .append("                                                    <div class=\"student-id\">ID: ").append(student.getUser_id()).append("</div>\n")
                    .append("                                                </div>\n")
                    .append("                                            </div>\n")
                    .append("                                        </td>\n")
                    .append("                                        <td><span class=\"highlight\">").append(score).append("</span></td>\n")
                    .append("                                    </tr>\n");
        }

        html.append("                                </tbody>\n")
                .append("                            </table>\n")
                .append("                        </div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("            <div class=\"col-md-6 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"card-header\">需要关注的学生</div>\n")
                .append("                    <div class=\"card-body\">\n")
                .append("                        <div class=\"table-responsive\">\n")
                .append("                            <table class=\"table table-hover\">\n")
                .append("                                <thead>\n")
                .append("                                    <tr>\n")
                .append("                                        <th>排名</th>\n")
                .append("                                        <th>学生信息</th>\n")
                .append("                                        <th>分数</th>\n")
                .append("                                    </tr>\n")
                .append("                                </thead>\n")
                .append("                                <tbody>\n");

        @SuppressWarnings("unchecked")
        List<Integer> bottomIndices = (List<Integer>) stats.get("bottomIndices");
        for (int i = 0; i < bottomIndices.size(); i++) {
            int index = bottomIndices.get(i);
            User student = students.get(index);
            int score = scores.get(index);

            String initials = "";
            if (student.getName() != null && !student.getName().isEmpty()) {
                String[] nameParts = student.getName().split(" ");
                for (String part : nameParts) {
                    if (!part.isEmpty()) {
                        initials += part.charAt(0);
                    }
                }
            } else if (student.getUsername() != null && !student.getUsername().isEmpty()) {
                initials = student.getUsername().substring(0, Math.min(2, student.getUsername().length()));
            } else {
                initials = "U" + student.getUser_id();
            }

            html.append("                                    <tr>\n")
                    .append("                                        <td><span class=\"badge bg-danger\">").append(students.size() - i).append("</span></td>\n")
                    .append("                                        <td>\n")
                    .append("                                            <div class=\"student-info\">\n")
                    .append("                                                <div class=\"student-avatar\">").append(initials).append("</div>\n")
                    .append("                                                <div>\n")
                    .append("                                                    <div class=\"student-name\">").append(student.getName() != null ? student.getName() : student.getUsername()).append("</div>\n")
                    .append("                                                    <div class=\"student-id\">ID: ").append(student.getUser_id()).append("</div>\n")
                    .append("                                                </div>\n")
                    .append("                                            </div>\n")
                    .append("                                        </td>\n")
                    .append("                                        <td>").append(score).append("</td>\n")
                    .append("                                    </tr>\n");
        }

        html.append("                                </tbody>\n")
                .append("                            </table>\n")
                .append("                        </div>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 难度分析
        html.append("        <div class=\"row\">\n")
                .append("            <div class=\"col-md-6 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"card-header\">最难的题目</div>\n")
                .append("                    <div class=\"card-body\">\n");

        int hardestIndex = (int) stats.get("hardestProblemIndex");
        if (hardestIndex < problems.size()) {
            Problem hardestProblem = problems.get(hardestIndex);
            html.append("                        <h5 class=\"mb-3\">题目 #").append(hardestIndex + 1).append("</h5>\n")
                    .append("                        <div class=\"problem-detail\">\n")
                    .append("                            <p><strong>描述:</strong> ").append(hardestProblem.getDescription()).append("</p>\n")
                    .append("                            <p><strong>通过率:</strong> <span class=\"badge bg-danger\">").append(df.format(ratios.get(hardestIndex) * 100)).append("%</span></p>\n")
                    .append("                            <p><strong>平均分:</strong> ").append(df.format(averages.get(hardestIndex))).append("</p>\n");

            if (hardestProblem.getAnalysis() != null && !hardestProblem.getAnalysis().isEmpty()) {
                html.append("                            <p><strong>分析:</strong> ").append(hardestProblem.getAnalysis()).append("</p>\n");
            }

            html.append("                            <p><strong>教学建议:</strong> 该题目通过率较低，建议在今后的教学中加强相关知识点的讲解和练习。可以考虑提供更多类似的练习题，或者在课堂上进行专项讲解。</p>\n")
                    .append("                        </div>\n");
        } else {
            html.append("                        <p>没有题目数据</p>\n");
        }

        html.append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("            <div class=\"col-md-6 mb-4\">\n")
                .append("                <div class=\"card h-100\">\n")
                .append("                    <div class=\"card-header\">最简单的题目</div>\n")
                .append("                    <div class=\"card-body\">\n");

        int easiestIndex = (int) stats.get("easiestProblemIndex");
        if (easiestIndex < problems.size()) {
            Problem easiestProblem = problems.get(easiestIndex);
            html.append("                        <h5 class=\"mb-3\">题目 #").append(easiestIndex + 1).append("</h5>\n")
                    .append("                        <div class=\"problem-detail\">\n")
                    .append("                            <p><strong>描述:</strong> ").append(easiestProblem.getDescription()).append("</p>\n")
                    .append("                            <p><strong>通过率:</strong> <span class=\"badge bg-success\">").append(df.format(ratios.get(easiestIndex) * 100)).append("%</span></p>\n")
                    .append("                            <p><strong>平均分:</strong> ").append(df.format(averages.get(easiestIndex))).append("</p>\n");

            if (easiestProblem.getAnalysis() != null && !easiestProblem.getAnalysis().isEmpty()) {
                html.append("                            <p><strong>分析:</strong> ").append(easiestProblem.getAnalysis()).append("</p>\n");
            }

            html.append("                            <p><strong>教学建议:</strong> 该题目通过率较高，说明学生对相关知识点掌握较好。可以考虑在今后的练习中增加此类题目的难度，或者减少类似题目的比重。</p>\n")
                    .append("                        </div>\n");
        } else {
            html.append("                        <p>没有题目数据</p>\n");
        }

        html.append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 教学建议
        html.append("        <div class=\"card\">\n")
                .append("            <div class=\"card-header\">教学建议</div>\n")
                .append("            <div class=\"card-body\">\n")
                .append("                <div class=\"row\">\n")
                .append("                    <div class=\"col-md-6\">\n")
                .append("                        <h5 class=\"mb-3\">针对题目的建议</h5>\n")
                .append("                        <ul class=\"list-group\">\n");

        @SuppressWarnings("unchecked")
        Map<String, Integer> difficultyDistribution = (Map<String, Integer>) stats.get("difficultyDistribution");
        int easyCount = difficultyDistribution.get("简单(通过率>80%)");
        int mediumCount = difficultyDistribution.get("中等(通过率40%-80%)");
        int hardCount = difficultyDistribution.get("困难(通过率<40%)");

        double easyPercentage = (double) easyCount / (double) problems.size() * 100;
        double hardPercentage = (double) hardCount / (double) problems.size() * 100;

        if (hardestIndex < problems.size()) {
            html.append("                            <li class=\"list-group-item\"><strong>针对最难题目 (题目 #").append(hardestIndex + 1).append(")：</strong> 建议在课堂上进行专项讲解，并提供更多类似的练习题。可以考虑分解该题目，逐步引导学生理解解题思路。</li>\n");
        }

        if (hardPercentage > 30) {
            html.append("                            <li class=\"list-group-item\"><strong>针对整体难度：</strong> 本次练习难度偏高，建议在今后的练习中适当降低难度，或者提供更多的辅助材料和预习资料。</li>\n");
        } else if (easyPercentage > 50) {
            html.append("                            <li class=\"list-group-item\"><strong>针对整体难度：</strong> 本次练习难度偏低，建议在今后的练习中适当提高难度，以更好地挑战学生能力。</li>\n");
        } else {
            html.append("                            <li class=\"list-group-item\"><strong>针对整体难度：</strong> 本次练习难度适中，建议保持这种难度分布，同时可以适当增加一些挑战性题目。</li>\n");
        }

        html.append("                            <li class=\"list-group-item\"><strong>题型分布：</strong> 建议在今后的练习中保持多样化的题型，以全面考察学生的能力。</li>\n")
                .append("                        </ul>\n")
                .append("                    </div>\n")
                .append("                    <div class=\"col-md-6\">\n")
                .append("                        <h5 class=\"mb-3\">针对学生的建议</h5>\n")
                .append("                        <ul class=\"list-group\">\n")
                .append("                            <li class=\"list-group-item\"><strong>针对成绩较低的学生：</strong> 建议进行针对性辅导，可以安排额外的辅导时间或提供补充材料。</li>\n")
                .append("                            <li class=\"list-group-item\"><strong>针对成绩优秀的学生：</strong> 可以提供更具挑战性的拓展题目，激发他们的学习兴趣。</li>\n")
                .append("                            <li class=\"list-group-item\"><strong>小组学习：</strong> 可以组织小组讨论，让成绩好的学生帮助成绩较差的学生，促进互助学习。</li>\n")
                .append("                            <li class=\"list-group-item\"><strong>针对薄弱知识点：</strong> 建议在课堂上加强对薄弱知识点的讲解和练习。</li>\n")
                .append("                        </ul>\n")
                .append("                    </div>\n")
                .append("                </div>\n")
                .append("            </div>\n")
                .append("        </div>\n");

        // 页脚
        html.append("        <div class=\"footer\">\n")
                .append("            <p class=\"mb-0\">© ").append(java.time.Year.now().getValue()).append(" 练习反馈报告系统 | 生成时间: ").append(dateFormat.format(new Date())).append("</p>\n")
                .append("            <p class=\"mb-0\">本报告仅供教学参考，请结合实际情况进行分析。</p>\n")
                .append("        </div>\n")
                .append("    </div>\n");

        // JavaScript 图表
        html.append("    <script>\n")
                .append("        // 设置全局Chart.js配置\n")
                .append("        Chart.defaults.font.family = \"'Segoe UI', Tahoma, Geneva, Verdana, sans-serif\";\n")
                .append("        Chart.defaults.font.size = 14;\n")
                .append("        Chart.defaults.color = '#333333';\n")
                .append("        \n")
                .append("        // 分数分布图表\n")
                .append("        const scoreDistributionCtx = document.getElementById('scoreDistributionChart').getContext('2d');\n")
                .append("        const scoreDistributionChart = new Chart(scoreDistributionCtx, {\n")
                .append("            type: 'doughnut',\n")
                .append("            data: {\n")
                .append("                labels: [");

        boolean first = true;
        @SuppressWarnings("unchecked")
        Map<String, Integer> scoreDistribution = (Map<String, Integer>) stats.get("scoreDistribution");
        for (String label : scoreDistribution.keySet()) {
            if (!first) {
                html.append(", ");
            }
            html.append("'").append(label).append("'");
            first = false;
        }

        html.append("],\n")
                .append("                datasets: [{\n")
                .append("                    data: [");

        first = true;
        for (Integer value : scoreDistribution.values()) {
            if (!first) {
                html.append(", ");
            }
            html.append(value);
            first = false;
        }

        html.append("],\n")
                .append("                    backgroundColor: ['#6a5acd', '#9370db', '#b19cd9', '#d8bfd8', '#e6e6fa'],\n")
                .append("                    borderWidth: 0,\n")
                .append("                    hoverOffset: 10\n")
                .append("                }]\n")
                .append("            },\n")
                .append("            options: {\n")
                .append("                responsive: true,\n")
                .append("                maintainAspectRatio: false,\n")
                .append("                plugins: {\n")
                .append("                    legend: {\n")
                .append("                        position: 'right',\n")
                .append("                        labels: {\n")
                .append("                            padding: 20,\n")
                .append("                            usePointStyle: true,\n")
                .append("                            pointStyle: 'circle'\n")
                .append("                        }\n")
                .append("                    },\n")
                .append("                    title: {\n")
                .append("                        display: true,\n")
                .append("                        text: '学生分数分布',\n")
                .append("                        font: {\n")
                .append("                            size: 16,\n")
                .append("                            weight: 'bold'\n")
                .append("                        },\n")
                .append("                        padding: {\n")
                .append("                            bottom: 20\n")
                .append("                        }\n")
                .append("                    }\n")
                .append("                },\n")
                .append("                cutout: '60%'\n")
                .append("            }\n")
                .append("        });\n");

        // 题目难度分布图表
        html.append("        // 题目难度分布图表\n")
                .append("        const difficultyDistributionCtx = document.getElementById('difficultyDistributionChart').getContext('2d');\n")
                .append("        const difficultyDistributionChart = new Chart(difficultyDistributionCtx, {\n")
                .append("            type: 'doughnut',\n")
                .append("            data: {\n")
                .append("                labels: [");

        first = true;
        for (String label : difficultyDistribution.keySet()) {
            if (!first) {
                html.append(", ");
            }
            html.append("'").append(label).append("'");
            first = false;
        }

        html.append("],\n")
                .append("                datasets: [{\n")
                .append("                    data: [");

        first = true;
        for (Integer value : difficultyDistribution.values()) {
            if (!first) {
                html.append(", ");
            }
            html.append(value);
            first = false;
        }

        html.append("],\n")
                .append("                    backgroundColor: ['#6a5acd', '#dda0dd', '#9932cc'],\n")
                .append("                    borderWidth: 0,\n")
                .append("                    hoverOffset: 10\n")
                .append("                }]\n")
                .append("            },\n")
                .append("            options: {\n")
                .append("                responsive: true,\n")
                .append("                maintainAspectRatio: false,\n")
                .append("                plugins: {\n")
                .append("                    legend: {\n")
                .append("                        position: 'right',\n")
                .append("                        labels: {\n")
                .append("                            padding: 20,\n")
                .append("                            usePointStyle: true,\n")
                .append("                            pointStyle: 'circle'\n")
                .append("                        }\n")
                .append("                    },\n")
                .append("                    title: {\n")
                .append("                        display: true,\n")
                .append("                        text: '题目难度分布',\n")
                .append("                        font: {\n")
                .append("                            size: 16,\n")
                .append("                            weight: 'bold'\n")
                .append("                        },\n")
                .append("                        padding: {\n")
                .append("                            bottom: 20\n")
                .append("                        }\n")
                .append("                    }\n")
                .append("                },\n")
                .append("                cutout: '60%'\n")
                .append("            }\n")
                .append("        });\n");

        // 题目通过率图表
        html.append("        // 题目通过率图表\n")
                .append("        const problemRatiosCtx = document.getElementById('problemRatiosChart').getContext('2d');\n")
                .append("        const problemRatiosChart = new Chart(problemRatiosCtx, {\n")
                .append("            type: 'bar',\n")
                .append("            data: {\n")
                .append("                labels: [");

        for (int i = 0; i < problems.size(); i++) {
            if (i > 0) {
                html.append(", ");
            }
            html.append("'题目 ").append(i + 1).append("'");
        }

        html.append("],\n")
                .append("                datasets: [{\n")
                .append("                    label: '通过率 (%)',\n")
                .append("                    data: [");

        for (int i = 0; i < ratios.size(); i++) {
            if (i > 0) {
                html.append(", ");
            }
            html.append(df.format(ratios.get(i) * 100));
        }

        html.append("],\n")
                .append("                    backgroundColor: 'rgba(123, 104, 238, 0.7)',\n")
                .append("                    borderColor: 'rgba(123, 104, 238, 1)',\n")
                .append("                    borderWidth: 1,\n")
                .append("                    borderRadius: 4,\n")
                .append("                    yAxisID: 'y'\n")
                .append("                }, {\n")
                .append("                    label: '平均分',\n")
                .append("                    data: [");

        for (int i = 0; i < averages.size(); i++) {
            if (i > 0) {
                html.append(", ");
            }
            html.append(averages.get(i));
        }

        html.append("],\n")
                .append("                    type: 'line',\n")
                .append("                    borderColor: 'rgba(75, 192, 192, 1)',\n")
                .append("                    borderWidth: 2,\n")
                .append("                    fill: false,\n")
                .append("                    yAxisID: 'y1'\n")
                .append("                }]\n")
                .append("            },\n")
                .append("            options: {\n")
                .append("                responsive: true,\n")
                .append("                maintainAspectRatio: false,\n")
                .append("                scales: {\n")
                .append("                    x: {\n")
                .append("                        title: {\n")
                .append("                            display: true,\n")
                .append("                            text: '题目编号'\n")
                .append("                        }\n")
                .append("                    },\n")
                .append("                    y: {\n")
                .append("                        type: 'linear',\n")
                .append("                        position: 'left',\n")
                .append("                        title: {\n")
                .append("                            display: true,\n")
                .append("                            text: '通过率 (%)'\n")
                .append("                        },\n")
                .append("                        beginAtZero: true,\n")
                .append("                        max: 100\n")
                .append("                    },\n")
                .append("                    y1: {\n")
                .append("                        type: 'linear',\n")
                .append("                        position: 'right',\n")
                .append("                        title: {\n")
                .append("                            display: true,\n")
                .append("                            text: '平均分'\n")
                .append("                        },\n")
                .append("                        grid: {\n")
                .append("                            drawOnChartArea: false\n")
                .append("                        }\n")
                .append("                    }\n")
                .append("                },\n")
                .append("                plugins: {\n")
                .append("                    title: {\n")
                .append("                        display: true,\n")
                .append("                        text: '题目通过率与平均分对比',\n")
                .append("                        font: {\n")
                .append("                            size: 16,\n")
                .append("                            weight: 'bold'\n")
                .append("                        },\n")
                .append("                        padding: {\n")
                .append("                            bottom: 20\n")
                .append("                        }\n")
                .append("                    },\n")
                .append("                    legend: {\n")
                .append("                        position: 'bottom'\n")
                .append("                    }\n")
                .append("                }\n")
                .append("            }\n")
                .append("        });\n")
                .append("    </script>\n")
                .append("</body>\n")
                .append("</html>\n");

        return html.toString();
    }
}