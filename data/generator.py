from generator_methods import generate_gift_certificates, generate_tags, generate_certificate_tags, \
    generate_users, generate_orders_and_items

tags_amount = 10

certificates_amount = 10
min_certificate_tags = 0
max_certificate_tags = 3

users_amount = 10

min_orders_amount = 0
max_orders_amount = 10

min_order_items_amount = 1
max_order_items_amount = 5

lines = ["USE certificates;"]
lines.append("")
lines.append("SET FOREIGN_KEY_CHECKS = 0;")
lines.append("TRUNCATE TABLE tags;")
lines.append("TRUNCATE TABLE gift_certificates;")
lines.append("TRUNCATE TABLE gift_certificates_tags;")
lines.append("TRUNCATE TABLE users;")
lines.append("TRUNCATE TABLE orders;")
lines.append("TRUNCATE TABLE order_items;")
lines.append("SET FOREIGN_KEY_CHECKS = 1;")
lines.append("")

tag_lines = generate_tags(records_amount=tags_amount, name_max_len=50)
for tag in tag_lines:
    lines.append(tag)
lines.append("")

certificate_lines = generate_gift_certificates(records_amount=certificates_amount, name_max_len=50,
                                               duration_max_len=500,
                                               min_price=9.99, max_price=10000, min_duration=29, max_duration=1096)
for certificate in certificate_lines:
    lines.append(certificate)
lines.append("")

certificate_tags_lines = generate_certificate_tags(certificates_amount=certificates_amount, tags_amount=tags_amount,
                                                   min_certificate_tags=min_certificate_tags,
                                                   max_certificate_tags=max_certificate_tags)
for certificate_tag in certificate_tags_lines:
    lines.append(certificate_tag)
lines.append("")

user_lines = generate_users(records_amount=users_amount, login_max_len=35, name_max_len=35,
                            surname_max_len=55, mail_max_len=200)
for user in user_lines:
    lines.append(user)
lines.append("")

order_lines, order_item_lines = generate_orders_and_items(users_amount=users_amount, certificates_amount=certificates_amount,
                                                          min_orders_amount=min_orders_amount, max_orders_amount=max_orders_amount,
                                                          min_items_amount=min_order_items_amount, max_items_amount=max_order_items_amount,
                                                          min_price=9.99, max_price=10000)
for order in order_lines:
    lines.append(order)
lines.append("")

for order_item in order_item_lines:
    lines.append(order_item)

for line in lines:
    print(line)

f = open('data.sql','w')
for request in lines:
    f.write(request + '\n')
f.close()
f = open('data.txt','w')
for request in lines:
    f.write(request + '\n')
f.close()
