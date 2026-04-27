-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Host: xfit-mysql:3306
-- Generation Time: Apr 22, 2026 at 01:07 PM
-- Server version: 8.0.43
-- PHP Version: 8.3.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `graymentality`
--

-- --------------------------------------------------------

--
-- Table structure for table `gm_slides`
--

CREATE TABLE `gm_slides` (
  `id` int NOT NULL,
  `slug` varchar(80) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sort_order` int NOT NULL DEFAULT '10',
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `kicker` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `title` varchar(120) COLLATE utf8mb4_unicode_ci NOT NULL,
  `subtitle` varchar(160) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `body` text COLLATE utf8mb4_unicode_ci,
  `bg_image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bg_overlay` tinyint(1) NOT NULL DEFAULT '1',
  `cta1_label` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cta1_href` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cta2_label` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `cta2_href` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Dumping data for table `gm_slides`
--

INSERT INTO `gm_slides` (`id`, `slug`, `sort_order`, `active`, `kicker`, `title`, `subtitle`, `body`, `bg_image`, `bg_overlay`, `cta1_label`, `cta1_href`, `cta2_label`, `cta2_href`, `created_at`, `updated_at`) VALUES
(1, 'welcome', 10, 1, 'WELCOME', 'GRAY MENTALITY', 'Strength is not loud. It’s consistent.', 'A framework for capability, clarity, and long-term resilience — built through discipline, not extremes.', 'assets/gm_1.jpg', 1, 'Enter', 'index.php#start', 'Read the Philosophy', 'index.php#philosophy', '2025-12-15 15:46:14', NULL),
(2, 'gray-zone', 20, 1, 'THE IDEA', 'THE GRAY ZONE', 'Not maximal. Not minimal. Intentional.', 'Most people live in black and white. Gray Mentality lives in adaptation, awareness, and sustainable effort.', 'assets/gm_2.jpg', 1, 'Why “Gray”?', 'index.php#philosophy', 'Next', '#', '2025-12-15 15:46:14', NULL),
(3, 'capability', 30, 1, 'THE BODY', 'CAPABILITY > APPEARANCE', 'Train for what life demands.', 'Strength is being able to do what matters today, tomorrow, and decades from now. Training is the tool. Longevity is the goal.', 'assets/gm_3.jpg', 1, 'Start Training', 'index.php#start', 'Explore', 'index.php#system', '2025-12-15 15:46:14', NULL),
(4, 'discipline', 40, 1, 'THE MIND', 'DISCIPLINE WITHOUT DOGMA', 'No hype. No punishment. No ego.', 'Just systems that work when motivation doesn’t — and habits that don’t break when life gets busy.', 'assets/gm_4.jpg', 1, 'The System', 'index.php#system', 'Next', '#', '2025-12-15 15:46:14', NULL),
(5, 'system', 50, 1, 'THE SYSTEM', 'STRUCTURE CREATES FREEDOM', 'Remove friction. Keep momentum.', 'Programs, tracking, reflection — not to control you, but to make progress automatic.', 'assets/gm_5.jpg', 1, 'What’s inside', 'index.php#system', 'Next', '#', '2025-12-15 15:46:14', NULL),
(6, 'begin', 60, 1, 'BEGIN', 'ENTER THE GRAY', 'Start where you are. Train for life.', 'Small actions, repeated, become identity. This is the long game — and it’s winnable.', 'assets/gm_6.jpg', 1, 'Get Started', 'index.php#start', 'Contact', 'index.php#contact', '2025-12-15 15:46:14', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `gm_slides`
--
ALTER TABLE `gm_slides`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `slug` (`slug`),
  ADD KEY `active` (`active`,`sort_order`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `gm_slides`
--
ALTER TABLE `gm_slides`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
