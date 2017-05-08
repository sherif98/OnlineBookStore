CREATE DEFINER=`root`@`localhost` PROCEDURE `buy_book`(customer_id int, ISBN int, quantity int)
BEGIN
	declare price real;
    select book.selling_price from book where book.ISBN = ISBN into price;
	insert into cart (customer_id, ISBN, quantity, selling_price) values(customer_id, ISBN, quantity, price * quantity);
END