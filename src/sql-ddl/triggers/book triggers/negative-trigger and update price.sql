CREATE DEFINER=`root`@`localhost` TRIGGER `project`.`book_BEFORE_UPDATE` BEFORE UPDATE ON `book` FOR EACH ROW
BEGIN
	if (NEW.quantity < 0) then
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'quantity negative';
    end if;
    
    if NEW.selling_price != OLD.selling_price then
		update cart
		set cart.selling_price = cart.quantity * NEW.selling_price
		where cart.ISBN = NEW.ISBN;
	end if;
END