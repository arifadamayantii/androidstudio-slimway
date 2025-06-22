-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 21, 2025 at 06:46 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_slimway`
--

-- --------------------------------------------------------

--
-- Table structure for table `detail_pesanan_katering`
--

CREATE TABLE `detail_pesanan_katering` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `menu_id` int(11) NOT NULL,
  `menu_name` varchar(100) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `calories` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detail_pesanan_katering`
--

INSERT INTO `detail_pesanan_katering` (`id`, `order_id`, `menu_id`, `menu_name`, `price`, `calories`) VALUES
(8, 6, 9, 'Salad', 20000.00, 323),
(11, 7, 9, 'Salad', 20000.00, 323),
(12, 8, 11, 'Nasi Goreng Udang', 35000.00, 342),
(13, 8, 13, 'Oat Meal Katsu', 32000.00, 200),
(14, 8, 14, 'Sup Kari Kubis', 26000.00, 317);

-- --------------------------------------------------------

--
-- Table structure for table `katering`
--

CREATE TABLE `katering` (
  `id` int(11) NOT NULL,
  `email_user` varchar(100) NOT NULL,
  `menu_category` varchar(100) NOT NULL,
  `package_option` varchar(100) NOT NULL,
  `menu_name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `calories` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `store_location` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `katering`
--

INSERT INTO `katering` (`id`, `email_user`, `menu_category`, `package_option`, `menu_name`, `description`, `calories`, `price`, `image_path`, `store_location`) VALUES
(9, 'rion19@gmail.com', 'Main Course', 'Medium', 'Caesar Salad', 'Salad', 323, 20000.00, 'uploads/katering/menu_1749960118_684e45b6b2267.jpg', 'JL. Mawar anggrek surabaya'),
(10, 'rion19@gmail.com', 'Main Course', 'Small', 'Tuna Sandwich Health', 'Tuna Sandwich ini memiliki cita rasa yang khas dengan smoky dari tuna yang dipanggang dengan kaya akan rempah-rempah.', 236, 25000.00, 'uploads/katering/menu_1749980309_684e9495a7f23.jpg', 'Kertajaya'),
(11, 'rion19@gmail.com', 'Main Course', 'Medium', 'Nasi Goreng Udang', 'Nasi goreng udang ini rendah kalori namun tinggi protein.', 342, 35000.00, 'uploads/katering/menu_1750046113_684f95a15fda8.jpg', 'Arina Residence, Kertajaya'),
(12, 'rion19@gmail.com', 'Dessert', 'Small', 'Fudgy Brownies', 'Fudgy Brownies merupakan dessert atau cemilan kaya akan cita rasa coklat.', 254, 29000.00, 'uploads/katering/menu_1750046201_684f95f997fda.jpg', 'Arina Residence, Kertajaya'),
(13, 'rion19@gmail.com', 'Main Course', 'Large', 'Oat Meal Katsu', 'Katsu yang biasanya dibuat melalui ayam, menu ini mengusung konsep rendah kalori sehingga yang digunakan ialah oat meal.', 200, 32000.00, 'uploads/katering/menu_1750046289_684f965164b36.jpg', 'Arina Residence, Kertajaya'),
(14, 'rion19@gmail.com', 'Main Course', 'Large', 'Sup Kari Kubis', 'Sup Kari Kubis ini mengandung banyak serat namun rendah kalori sehingga aman untuk dikonsumsi setiap hari tanpa cemas berat badan akan naik.', 317, 26000.00, 'uploads/katering/menu_1750046371_684f96a360990.jpg', 'Arina Residence, Kertajaya');

-- --------------------------------------------------------

--
-- Table structure for table `pesanan_katering`
--

CREATE TABLE `pesanan_katering` (
  `order_id` int(11) NOT NULL,
  `emailUser` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `address` text NOT NULL,
  `city` varchar(50) NOT NULL,
  `province` varchar(50) NOT NULL,
  `postalCode` varchar(10) NOT NULL,
  `phoneNumber` varchar(20) NOT NULL,
  `selected_plan` varchar(50) NOT NULL,
  `deliveryTime` varchar(20) NOT NULL,
  `startDate` varchar(20) NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `selected_bank` varchar(50) DEFAULT NULL,
  `card_number` varchar(30) DEFAULT NULL,
  `coin_value` int(11) DEFAULT 0,
  `original_price` decimal(10,2) NOT NULL,
  `final_price` decimal(10,2) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `pesanan_katering`
--

INSERT INTO `pesanan_katering` (`order_id`, `emailUser`, `name`, `address`, `city`, `province`, `postalCode`, `phoneNumber`, `selected_plan`, `deliveryTime`, `startDate`, `payment_method`, `selected_bank`, `card_number`, `coin_value`, `original_price`, `final_price`, `created_at`) VALUES
(6, 'naotomori19@gmail.com', 'Nao', 'JL. mawar', 'surabaya', 'jawa timur', '0981', '0986161772', 'weekly', '11.00', '12-03-2024', 'COD', '', '', 45, 20000.00, 20000.00, '2025-06-15 03:45:57'),
(7, 'naotomori19@gmail.com', 'Puput Nur Fadilah', 'JL. Medokan Asri No. 12, Rungkut', 'Surabaya', 'Jawa Timur', '60222', '081232459087', 'weekly', '13.00', '20 Juni 2025', 'COD', '', '', 45, 73000.00, 73000.00, '2025-06-15 09:34:47'),
(8, 'jeremy@gmail.com', 'Jeremy Owen', 'Jl. Bakti Indah No 27, Jakarta Barat', 'Jakarta', 'DKI Jakarta', '60178', '0826281916', 'weekly', '13.00', '28 Juni 2025', 'COD', '', '', 0, 93000.00, 93000.00, '2025-06-16 14:01:05');

-- --------------------------------------------------------

--
-- Table structure for table `recipes`
--

CREATE TABLE `recipes` (
  `id` int(11) NOT NULL,
  `recipe_name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `photo_path` varchar(255) DEFAULT NULL,
  `protein` float DEFAULT NULL,
  `fat` float DEFAULT NULL,
  `carbs` float DEFAULT NULL,
  `calories` float DEFAULT NULL,
  `is_breakfast` tinyint(1) DEFAULT 0,
  `is_lunch` tinyint(1) DEFAULT 0,
  `is_dinner` tinyint(1) DEFAULT 0,
  `ingredients` text DEFAULT NULL,
  `instructions` text DEFAULT NULL,
  `video_path` varchar(255) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `status` enum('draft','upload') NOT NULL DEFAULT 'draft'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `recipes`
--

INSERT INTO `recipes` (`id`, `recipe_name`, `description`, `photo_path`, `protein`, `fat`, `carbs`, `calories`, `is_breakfast`, `is_lunch`, `is_dinner`, `ingredients`, `instructions`, `video_path`, `email`, `status`) VALUES
(52, 'Salad', 'Salad', 'uploads/recipes/recipe_1749946996_684e1274425dd.jpg', 12, 12, 12, 321, 1, 1, 0, 'Salad', 'Salad', 'default.mp4', 'naotomori19@gmail.com', 'draft'),
(54, 'Salad', 'Salad', 'uploads/recipes/recipe_1749950884_684e21a44cee0.jpg', 12, 12, 12, 321, 1, 1, 0, 'Salad', 'Salad', 'default.mp4', 'naotomori19@gmail.com', 'draft'),
(55, 'Ayam Bakar Madu Lezat', 'Ayam panggang dengan bumbu madu dan kecap.', 'uploads/recipes/recipe_1749951413_684e23b52315d.jpg', 12, 2, 40, 405, 1, 1, 0, 'Ayam, Madu, Kecap, Bawang Putih', 'Marinasi ayam dengan bumbu.\n\nBakar ayam hingga matang.\n\nSajikan dengan nasi putih.', 'default.mp4', 'naotomori19@gmail.com', 'upload'),
(59, 'Tofu Soup', 'Tofu Soup adalah makanan yang tinggi protein namun juga rendah kalori karena dibuat dengan cintahhhh', 'uploads/recipes/recipe_1749979877_684e92e572f6b.jpg', 12, 12, 12, 12, 1, 0, 0, 'tahu susu, wortel', 'oseng oseng di wajan', 'default.mp4', 'naotomori19@gmail.com', 'upload'),
(60, 'Red Velvet Cake', 'hahshwus', 'uploads/recipes/recipe_1750044585_684f8fa9d3df5.jpg', 12, 12, 12, 12, 0, 0, 1, 'hwhdhdhx', 'hshdhhdu', 'default.mp4', 'lala@gmail.com', 'upload'),
(61, 'cupcake', 'shajhs', 'uploads/recipes/recipe_1750044738_684f9042c4827.jpg', 12, 12, 12, 12, 0, 0, 1, 'hshshd', 'shhahdhd', 'default.mp4', 'lala@gmail.com', 'upload'),
(62, 'Mie Jebew', 'mantul', 'uploads/recipes/recipe_1750082598_685024264a5de.jpg', 21, 42, 35, 651, 0, 1, 1, 'mie', 'iseng-iseng', 'default.mp4', 'jeremy@gmail.com', 'upload');

-- --------------------------------------------------------

--
-- Table structure for table `rewards`
--

CREATE TABLE `rewards` (
  `id` int(11) NOT NULL,
  `email` varchar(150) NOT NULL,
  `points` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rewards`
--

INSERT INTO `rewards` (`id`, `email`, `points`) VALUES
(1, 'naotomori19@gmail.com', 45),
(2, 'reginarena21@gmail.com', 0),
(3, 'p@gmail.com', 5),
(4, 'lala@gmail.com', 10),
(5, 'jeremy@gmail.com', 5);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status_akun` varchar(50) NOT NULL DEFAULT 'user',
  `status` tinyint(1) NOT NULL DEFAULT 1,
  `failed_attempts` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `first_name`, `last_name`, `email`, `phone`, `password`, `status_akun`, `status`, `failed_attempts`) VALUES
(1, 'novia', 'ramadhani', 'ramadhaninovia911@gmail.com', '083857108381', 'r4m4dh4n1', 'user', 1, 0),
(2, 'Regina', 'Rena', 'reginarena21@gmail.com', '083857108380', '123456', 'user', 1, 0),
(3, 'Nao', 'Tomori', 'naotomori19@gmail.com', '083857108000', '123456', 'user', 1, 0),
(4, 'Widya', 'Ningrum', 'widyaningrum@gmail.com', '081234567890', '123456', 'admin', 1, 0),
(5, 'ayu', 'yuyu', 'ayu@gmail.com', '081232459087', 'ayu123', 'user', 1, 0),
(6, 'lula', 'lula', 'lula@gmail.com', '08123456328', 'lula', 'user', 1, 0),
(8, 'yaya', 'yuyu', 'yaya@gmail.com', '081234572527', 'yaya', 'user', 1, 0),
(9, 'kamari', 'kamari', 'kamari@gmail.com', '081235362527', 'kamari', 'user', 1, 0),
(10, 'yaya', 'kamari', 'yaka@gmail.com', '082425671527', 'yaka', 'user', 1, 0),
(11, 'puput', 'nur', 'puput@gmail.com', '082415263735', 'puput', 'user', 1, 0),
(14, 'maya', 'maya', 'maya@gmail.com', '02823728283', 'maya', 'admin', 1, 0),
(16, 'Enomoto', 'Rion', 'rion19@gmail.com', '08123838293', '123456', 'mitra', 1, 0),
(18, 'Lala', 'nusa', 'lala@gmail.com', '08291829', '123456', 'user', 1, 0),
(19, 'rara', 'nusa', 'nusa@gmail.com', '0829128912', '123', 'user', 1, 0),
(20, 'nusa', 'lala', 'n@gmail.com', '91829819891', '1', 'user', 1, 0),
(25, 'Oren', 'Hikaru', 'oren@gmail.com', '085124646434', 'oren', 'admin', 1, 0),
(27, 'Jeremy', 'Owen Nicholas', 'jeremy@gmail.com', '08454564343', 'jeremy', 'user', 1, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detail_pesanan_katering`
--
ALTER TABLE `detail_pesanan_katering`
  ADD PRIMARY KEY (`id`),
  ADD KEY `order_id` (`order_id`),
  ADD KEY `menu_id` (`menu_id`);

--
-- Indexes for table `katering`
--
ALTER TABLE `katering`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pesanan_katering`
--
ALTER TABLE `pesanan_katering`
  ADD PRIMARY KEY (`order_id`);

--
-- Indexes for table `recipes`
--
ALTER TABLE `recipes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `rewards`
--
ALTER TABLE `rewards`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detail_pesanan_katering`
--
ALTER TABLE `detail_pesanan_katering`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `katering`
--
ALTER TABLE `katering`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `pesanan_katering`
--
ALTER TABLE `pesanan_katering`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `recipes`
--
ALTER TABLE `recipes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT for table `rewards`
--
ALTER TABLE `rewards`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detail_pesanan_katering`
--
ALTER TABLE `detail_pesanan_katering`
  ADD CONSTRAINT `detail_pesanan_katering_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `pesanan_katering` (`order_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `detail_pesanan_katering_ibfk_2` FOREIGN KEY (`menu_id`) REFERENCES `katering` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
