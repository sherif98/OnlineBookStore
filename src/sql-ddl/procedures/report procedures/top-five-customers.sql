CREATE DEFINER=`root`@`localhost` PROCEDURE `top_five_customers`()
BEGIN
	set sql_safe_updates = 0;
	delete from sold
	where (DATEDIFF(CURDATE(), sold.selling_date) / 30) >= 3;
    
    set sql_safe_updates = 1;
    
	select customer.id, customer.first_name ,customer.last_name, SUM(sold.selling_price) as total_cash
    from customer join sold on customer.id = sold.customer_id
    group by customer.id
    order by total_cash DESC
    limit 5;
END