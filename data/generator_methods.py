import inflect
import random
import names

def generate_gift_certificates(records_amount, name_max_len, duration_max_len,
                               min_price, max_price, min_duration, max_duration):
    p = inflect.engine()

    names = []
    descriptions = []
    prices = []
    durations = []
    for i in range(records_amount):
        line = p.number_to_words(i + 1)
        line = line.replace(",", "")
        line = line.replace("-", " ")
        line = "certificate " + line
        names.append(line)
        descriptions.append("description")
        prices.append(str(random.randint(min_price * 100, max_price) / 100))
        durations.append(str(random.randint(min_duration, max_duration)))

    requests = []
    tag_insert_template = "INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date) " \
                          "VALUES('{name}', '{description}', '{price}', '{duration}', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3));"
    for i in range(records_amount):
        request = tag_insert_template.format(name=names[i], description=descriptions[i], price=prices[i],
                                             duration=durations[i])
        requests.append(request)

    return requests

def generate_tags(records_amount, name_max_len):
    p = inflect.engine()

    names = []
    for i in range(records_amount):
        line = p.number_to_words(i + 1)
        line = line.replace(",", "")
        line = line.replace("-", " ")
        line = "tag " + line
        names.append(line)

    requests = []
    tag_insert_template = "INSERT INTO tags(name) VALUES('{name}');"
    for name in names:
        request = tag_insert_template.format(name=name)
        requests.append(request)

    return requests

def generate_certificate_tags(certificates_amount, tags_amount, min_certificate_tags, max_certificate_tags):

    requests = []
    certificate_tags = "INSERT INTO gift_certificates_tags(id_gift_certificate, id_tag) "\
                                       "VALUES('{certificate_id}', '{tag_id}');"
    for i in range(certificates_amount):
        dict = {}
        certificate_tags_amount = random.randint(min_certificate_tags, max_certificate_tags)
        while len(dict) < certificate_tags_amount:
            dict[random.randint(1, tags_amount)] = 0

        for tag_id in dict.keys():
            request = certificate_tags.format(certificate_id=i+1, tag_id=tag_id)
            requests.append(request)

    return requests

def generate_users(records_amount, login_max_len, name_max_len, surname_max_len, mail_max_len):
    login_lines = []
    name_lines = []
    surname_lines = []
    mail_lines = []
    for i in range(records_amount):
        first_name = str(names.get_first_name())
        last_name = str(names.get_last_name())
        login_lines.append(first_name.lower() + "_" + last_name.lower())
        name_lines.append(first_name)
        surname_lines.append(last_name)
        mail_lines.append(first_name.lower() + "." + last_name.lower() + "@gmail.com")

    requests = []
    user_insert_template = "INSERT INTO users(login, name, surname, email) VALUES('{login}', '{name}', '{surname}', '{mail}');"
    for i in range(records_amount):
        request = user_insert_template.format(login=login_lines[i], name=name_lines[i], surname=surname_lines[i],
                                              mail=mail_lines[i])
        requests.append(request)
    return requests

def generate_orders_and_items(users_amount, certificates_amount,
                              min_orders_amount, max_orders_amount, min_items_amount, max_items_amount,
                              min_price, max_price):
    order_requests = []
    order_template = "INSERT INTO orders(id, id_user, create_order_time, update_order_time) " \
                       "VALUES('{id}', '{user_id}', CURRENT_TIMESTAMP(3), CURRENT_TIMESTAMP(3));"
    order_item_requests = []
    order_item_template = "INSERT INTO order_items(id_order, id_gift_certificate, price) " \
                     "VALUES('{order_id}', '{certificate_id}', '{price}');"
    current_order_id = 1
    for i in range(users_amount):
        for j in range(random.randint(min_orders_amount, max_orders_amount)):
            order_request = order_template.format(id=current_order_id, user_id=i+1)
            order_requests.append(order_request)

            order_certificates = {}
            certificate_tags_amount = random.randint(min_items_amount, max_items_amount)
            while len(order_certificates) < certificate_tags_amount:
                order_certificates[random.randint(1, certificates_amount)] = 0

            for certificate_id in order_certificates.keys():
                price = str(random.randint(min_price * 100, max_price) / 100)
                order_item_request = order_item_template.format(order_id=current_order_id,
                                                                certificate_id=certificate_id, price=price)
                order_item_requests.append(order_item_request)

            current_order_id += 1

    return order_requests, order_item_requests
