-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 25-Nov-2025 às 15:32
-- Versão do servidor: 10.4.32-MariaDB
-- versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `mist`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `console`
--

CREATE TABLE `console` (
  `id` int(11) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `preco` float NOT NULL,
  `marca_id` int(11) NOT NULL,
  `cor_id` int(11) NOT NULL,
  `ano` year(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Extraindo dados da tabela `console`
--

INSERT INTO `console` (`id`, `nome`, `preco`, `marca_id`, `cor_id`, `ano`) VALUES
(9, '111Console', 1111.11, 6, 1, '2155'),
(10, 'Labareta', 500, 7, 1, '1901');

-- --------------------------------------------------------

--
-- Estrutura da tabela `console_jogo`
--

CREATE TABLE `console_jogo` (
  `console_id` int(11) DEFAULT NULL,
  `jogo_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `cor`
--

CREATE TABLE `cor` (
  `id` int(11) NOT NULL,
  `cor` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Extraindo dados da tabela `cor`
--

INSERT INTO `cor` (`id`, `cor`) VALUES
(1, 'Verde'),
(2, 'Azul'),
(3, 'Vermelho'),
(4, 'Cinza'),
(5, 'Preto');

-- --------------------------------------------------------

--
-- Estrutura da tabela `jogo`
--

CREATE TABLE `jogo` (
  `id` int(11) NOT NULL,
  `nome` varchar(50) NOT NULL,
  `preco` float NOT NULL,
  `ano` year(4) NOT NULL,
  `capa` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura da tabela `marca`
--

CREATE TABLE `marca` (
  `id` int(11) NOT NULL,
  `marca` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Extraindo dados da tabela `marca`
--

INSERT INTO `marca` (`id`, `marca`) VALUES
(5, 'Nintendo'),
(6, 'Xbox'),
(7, 'PC'),
(8, 'Sony');

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `console`
--
ALTER TABLE `console`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`),
  ADD KEY `console_ibfk_1` (`cor_id`),
  ADD KEY `console_ibfk_2` (`marca_id`);

--
-- Índices para tabela `console_jogo`
--
ALTER TABLE `console_jogo`
  ADD KEY `console_id` (`console_id`),
  ADD KEY `jogo_id` (`jogo_id`);

--
-- Índices para tabela `cor`
--
ALTER TABLE `cor`
  ADD PRIMARY KEY (`id`);

--
-- Índices para tabela `jogo`
--
ALTER TABLE `jogo`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nome` (`nome`);

--
-- Índices para tabela `marca`
--
ALTER TABLE `marca`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `console`
--
ALTER TABLE `console`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de tabela `cor`
--
ALTER TABLE `cor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de tabela `jogo`
--
ALTER TABLE `jogo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `marca`
--
ALTER TABLE `marca`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restrições para despejos de tabelas
--

--
-- Limitadores para a tabela `console`
--
ALTER TABLE `console`
  ADD CONSTRAINT `console_ibfk_1` FOREIGN KEY (`cor_id`) REFERENCES `cor` (`id`),
  ADD CONSTRAINT `console_ibfk_2` FOREIGN KEY (`marca_id`) REFERENCES `marca` (`id`);

--
-- Limitadores para a tabela `console_jogo`
--
ALTER TABLE `console_jogo`
  ADD CONSTRAINT `console_jogo_ibfk_1` FOREIGN KEY (`console_id`) REFERENCES `console` (`id`),
  ADD CONSTRAINT `console_jogo_ibfk_2` FOREIGN KEY (`jogo_id`) REFERENCES `jogo` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
