import logging

logging.basicConfig(
    level    = logging.INFO,
    format   = '%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    filename = 'app.log',
    encoding = 'utf-8'
)

logger = logging.getLogger(__name__)
logger.info("服务启动成功")
