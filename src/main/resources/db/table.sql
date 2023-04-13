DROP TABLE IF EXISTS tb_pic;
CREATE TABLE tb_pic(
                       id INT PRIMARY KEY NOT NULL,
                       p_id INT NOT NULL DEFAULT '0' COMMENT '上级图片id',
                       pic_name VARCHAR(255) NOT NULL DEFAULT '' COMMENT '图片名称',
                       pic_type INT NOT NULL DEFAULT '1' COMMENT '图片类型 1:jpeg',
                       pic_level INT NOT NULL DEFAULT '1' COMMENT '图片层级 默认1级',
                       pic_save_path VARCHAR(255) NOT NULL DEFAULT '' COMMENT '图片位置',
                       create_time datetime,
                       camera_id INT NOT NULL DEFAULT '0' COMMENT '摄像头id');
