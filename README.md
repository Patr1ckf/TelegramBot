# Patricus Bot

This Telegram bot was created as a solution to help users manage their shopping lists and compare prices of products available online. The bot is designed to facilitate a streamlined process for creating shopping lists and obtaining the average price of desired products from various online sources.

## Features

### Shopping List Management
- `/makelist` - Initiate the process of creating a shopping list.
- `/showlist` - Display the last created shopping list.

### Price Comparison
- `/checkprice` - Check the average price of a specific product.
- `/checkpricelist` - Check the average prices of products in the last created shopping list and sum of costs

## Usage

The bot is easy to use:
1. Start the conversation with the bot by typing `/start`.
2. Use commands like `/makelist` to create a shopping list and add items.
3. Use `/checkprice` to compare prices of desired products.
4. View the last shopping list with `/showlist`.
5. Viev the last shopping list with average prices an sum of costs with `/checkpricelist`.

## Implementation

This bot was implemented in Java using the Telegram Bot API and utilizes various commands to manage shopping lists and retrieve product prices from online sources like Ceneo.pl. It employs web scraping techniques to extract price information from the website.

### Commands Package
- `MakeListCommand`: Manages the creation and addition of items to shopping lists.
- `GetPriceCommand`: Retrieves price information by scraping data from Ceneo.pl.
- `ShowListCommand`: Shows the last created shopping list.
- `StartCommand`: Welcomes new user and shows list of available commands.
- `ShowPriceListCommand`: Shows the last created shopping list with the average price of each product and calculate sum of shopping.

## Underlying Product Story

The motivation behind this bot is to simplify the process of creating shopping lists and checking product prices. It aims to assist users in efficiently managing their shopping needs by providing quick access to price comparisons for better decision-making.

## Core Problem Statement
The primary challenge/task behind the creation of this bot was to develop a solution capable of retrieving accurate and updated product prices from a specific website, employing web scraping techniques for data extraction from Ceneo.pl or similar online sources.
